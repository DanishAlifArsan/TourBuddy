package com.tourbuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tourbuddy.api.ListReviewItem
import com.tourbuddy.api.ListReviewResponse
import com.tourbuddy.data.Review
import com.tourbuddy.databinding.FragmentDetailBinding
import com.tourbuddy.databinding.FragmentReviewBinding
import com.tourbuddy.viewModel.DestinationViewModel
import com.tourbuddy.viewModel.DestinationViewModelFactory
import com.tourbuddy.viewModel.ListReviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ReviewFragment : Fragment() {
    private lateinit var binding : FragmentReviewBinding
    private lateinit var rvReview: RecyclerView
    private val list = ArrayList<ListReviewItem>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.btnWriteReview.setOnClickListener{
            val writeReviewFragment = WriteReviewFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, writeReviewFragment, WriteReviewFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        //mendapatkan token
        val mUser = auth.currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result.token

                    //retrofit untuk akses ke api
                    val listReviewViewModel = obtainViewModel(activity as AppCompatActivity)

                    rvReview = binding.rvDestination
                    rvReview.setHasFixedSize(true)

                    listReviewViewModel.review.observe(viewLifecycleOwner) {
                        list.addAll(it.listReview)
                        showRecyclerList()
                    }

                    listReviewViewModel.isLoading.observe(viewLifecycleOwner) {
                        showLoading(it)
                    }
                } else {
                    task.exception
                }
            }

//        rvReview = binding.rvDestination
//        rvReview.setHasFixedSize(true)
//
//        list.addAll(getListReview())
//        showRecyclerList()
    }

    private fun getListReview(): ArrayList<Review> {
        val dataName = resources.getStringArray(R.array.review_name)
        val dataReview = resources.getStringArray(R.array.review_review)
        val dataPhoto = resources.obtainTypedArray(R.array.review_photo)
        val dataRating = resources.getIntArray(R.array.review_rating)
        val listReview = ArrayList<Review>()
        for (i in dataName.indices) {
            val review = Review(dataName[i], dataReview[i], dataRating[i], dataPhoto.getResourceId(i, -1))
            listReview.add(review)
        }
        dataPhoto.recycle()
        return listReview
    }

    private fun showRecyclerList() {
        rvReview.layoutManager = LinearLayoutManager(requireContext())
        val listReviewAdapter = ListReviewAdapter(list)
        rvReview.adapter = listReviewAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity) : ListReviewViewModel {
        val factory = DestinationViewModelFactory.getInstance(activity)
        return ViewModelProvider(this, factory).get(ListReviewViewModel::class.java)
    }
    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}