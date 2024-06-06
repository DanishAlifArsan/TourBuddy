package com.tourbuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tourbuddy.api.ResultState
import com.tourbuddy.databinding.FragmentWriteReviewBinding
import com.tourbuddy.viewModel.ListReviewViewModel
import com.tourbuddy.viewModel.ReviewViewModelFactory
import com.tourbuddy.viewModel.ViewModelFactory
import com.tourbuddy.viewModel.AddReviewViewModel

class WriteReviewFragment : Fragment() {
    private lateinit var binding : FragmentWriteReviewBinding
    private var destinationId: String? = null
    private var rating: Int = 0
    private var review: String = ""
    private lateinit var addReviewViewModel : AddReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.star1.setOnClickListener {
            binding.star1.setImageResource(R.drawable.star_enabled)
            binding.star2.setImageResource(R.drawable.star_disable)
            binding.star3.setImageResource(R.drawable.star_disable)
            binding.star4.setImageResource(R.drawable.star_disable)
            binding.star5.setImageResource(R.drawable.star_disable)
            rating = 1
        }
        binding.star2.setOnClickListener {
            binding.star1.setImageResource(R.drawable.star_enabled)
            binding.star2.setImageResource(R.drawable.star_enabled)
            binding.star3.setImageResource(R.drawable.star_disable)
            binding.star4.setImageResource(R.drawable.star_disable)
            binding.star5.setImageResource(R.drawable.star_disable)
            rating = 2
        }
        binding.star3.setOnClickListener {
            binding.star1.setImageResource(R.drawable.star_enabled)
            binding.star2.setImageResource(R.drawable.star_enabled)
            binding.star3.setImageResource(R.drawable.star_enabled)
            binding.star4.setImageResource(R.drawable.star_disable)
            binding.star5.setImageResource(R.drawable.star_disable)
            rating = 3
        }
        binding.star4.setOnClickListener {
            binding.star1.setImageResource(R.drawable.star_enabled)
            binding.star2.setImageResource(R.drawable.star_enabled)
            binding.star3.setImageResource(R.drawable.star_enabled)
            binding.star4.setImageResource(R.drawable.star_enabled)
            binding.star5.setImageResource(R.drawable.star_disable)
            rating = 4
        }
        binding.star5.setOnClickListener {
            binding.star1.setImageResource(R.drawable.star_enabled)
            binding.star2.setImageResource(R.drawable.star_enabled)
            binding.star3.setImageResource(R.drawable.star_enabled)
            binding.star4.setImageResource(R.drawable.star_enabled)
            binding.star5.setImageResource(R.drawable.star_enabled)
            rating = 5
        }

        review = binding.edAddReview.text.toString()

        destinationId = arguments?.getString("destination_id")

        addReviewViewModel = obtainViewModel(activity as AppCompatActivity)


        binding.btnPostReview.setOnClickListener{
            addReviewViewModel.addReview(review, rating, destinationId).observe(viewLifecycleOwner) { result ->
                if (result != null){
                    when (result) {
                        is ResultState.Loading -> {
                        }
                        is ResultState.Success -> {
                            showToast(result.data.message)
                            val fragmentManager = parentFragmentManager
                            fragmentManager.popBackStack()
                        }
                        is ResultState.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : AddReviewViewModel {
        val factory = ReviewViewModelFactory.getInstance(activity)
        return ViewModelProvider(this, factory).get(AddReviewViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}