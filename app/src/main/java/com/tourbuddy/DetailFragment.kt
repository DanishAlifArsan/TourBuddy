package com.tourbuddy

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tourbuddy.api.ListDestinationsItem
import com.tourbuddy.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var binding : FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val destination = if (Build.VERSION.SDK_INT >= 33) {
                arguments?.getParcelable("key_destination", ListDestinationsItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable("key_destination")
            }

            val id = destination?.destinationId

            binding.tvName.text = destination?.destinationName
            binding.tvLocation.text = destination?.city
            binding.tvSubtitle.text = destination?.city
            binding.tvRating.text = destination?.rating.toString()
            binding.tvReviewCount.text = getString(R.string.hint_review_count, destination?.ratingCount.toString())
            binding.tvDescription.text = destination?.description

            binding.btnReview.setOnClickListener{
                val reviewFragment = ReviewFragment()
                val bundle = Bundle()
                bundle.putString("destination_id", id)
                reviewFragment.arguments = bundle

                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, reviewFragment, ReviewFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}