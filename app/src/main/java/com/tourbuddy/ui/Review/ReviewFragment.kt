package com.tourbuddy.ui.Review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourbuddy.R

import com.tourbuddy.api.response.ListReviewsItem
import com.tourbuddy.databinding.FragmentReviewBinding
import com.tourbuddy.ui.AddReview.WriteReviewFragment
import com.tourbuddy.viewModelFactory.ReviewViewModelFactory

class ReviewFragment : Fragment() {
    private lateinit var binding : FragmentReviewBinding
    private lateinit var rvReview: RecyclerView
    private val list = ArrayList<ListReviewsItem>()
    private lateinit var listReviewViewModel : ListReviewViewModel
    private var destinationId: String? = null
    private lateinit var listReviewAdapter : ListReviewAdapter
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
        rvReview.layoutManager = LinearLayoutManager(requireContext())
        rvReview.setHasFixedSize(true)

        listReviewViewModel = obtainViewModel(activity as AppCompatActivity)
        listReviewViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        listReviewViewModel.getAllReview(destinationId).observe(viewLifecycleOwner) {response ->

            listReviewAdapter = ListReviewAdapter()
            listReviewAdapter.submitList(response.listReviews.sortedByDescending { it.createdAt})
            rvReview.adapter = listReviewAdapter
        }

        binding.btnWriteReview.setOnClickListener{
            val writeReviewFragment = WriteReviewFragment()
            val bundle = Bundle()
            bundle.putString("destination_id", destinationId)
            writeReviewFragment.arguments = bundle

            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, writeReviewFragment, WriteReviewFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
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