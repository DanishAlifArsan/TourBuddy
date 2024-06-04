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

import com.tourbuddy.api.ListReviewResponse
import com.tourbuddy.api.ListReviewsItem
import com.tourbuddy.data.Review
import com.tourbuddy.databinding.FragmentDetailBinding
import com.tourbuddy.databinding.FragmentReviewBinding
import com.tourbuddy.viewModel.DestinationViewModel
import com.tourbuddy.viewModel.DestinationViewModelFactory
import com.tourbuddy.viewModel.ListReviewViewModel
import com.tourbuddy.viewModel.ReviewViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ReviewFragment : Fragment() {
    private lateinit var binding : FragmentReviewBinding
    private lateinit var rvReview: RecyclerView
    private val list = ArrayList<ListReviewsItem>()
    private lateinit var auth: FirebaseAuth
    private lateinit var listReviewViewModel : ListReviewViewModel
    private var destinationId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationId = arguments?.getString("destination_id")

        rvReview = binding.rvDestination
        rvReview.setHasFixedSize(true)

        listReviewViewModel = obtainViewModel(activity as AppCompatActivity)
        listReviewViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        listReviewViewModel.getAllReview(destinationId).observe(viewLifecycleOwner) {response ->
            list.addAll(response.listReviews)
            showRecyclerList()
        }

        binding.btnWriteReview.setOnClickListener{
            val writeReviewFragment = WriteReviewFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, writeReviewFragment, WriteReviewFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }


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
        val factory = ReviewViewModelFactory.getInstance(activity)
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