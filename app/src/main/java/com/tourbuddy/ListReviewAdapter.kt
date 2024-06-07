package com.tourbuddy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tourbuddy.api.ListDestinationsItem
import com.tourbuddy.api.ListReviewsItem

import com.tourbuddy.databinding.ReviewListItemBinding

class ListReviewAdapter: ListAdapter<ListReviewsItem, ListReviewAdapter.ListViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(holder, review)
    }

    class ListViewHolder(val binding: ReviewListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (holder : ListViewHolder, review : ListReviewsItem){
            binding.tvName.text = review.reviewerName
            binding.tvDate.text = review.createdAt
            binding.tvReview.text = review.review
            binding.tvRating.text = review.rating.toString()
            binding.ivItemPhoto.setImageResource(R.drawable.ic_face)

            val filledStarResId = R.drawable.star_enabled
            val unfilledStarResId = R.drawable.star_disable


            when(review.rating){
            5 -> {
                binding.star1.setImageResource(filledStarResId)
                binding.star2.setImageResource(filledStarResId)
                binding.star3.setImageResource(filledStarResId)
                binding.star4.setImageResource(filledStarResId)
                binding.star5.setImageResource(filledStarResId)
            }
            4 -> {
                binding.star1.setImageResource(filledStarResId)
                binding.star2.setImageResource(filledStarResId)
                binding.star3.setImageResource(filledStarResId)
                binding.star4.setImageResource(filledStarResId)
                binding.star5.setImageResource(unfilledStarResId)
            }
            3 -> {
                binding.star1.setImageResource(filledStarResId)
                binding.star2.setImageResource(filledStarResId)
                binding.star3.setImageResource(filledStarResId)
                binding.star4.setImageResource(unfilledStarResId)
                binding.star5.setImageResource(unfilledStarResId)
            }
            2 -> {
                binding.star1.setImageResource(filledStarResId)
                binding.star2.setImageResource(filledStarResId)
                binding.star3.setImageResource(unfilledStarResId)
                binding.star4.setImageResource(unfilledStarResId)
                binding.star5.setImageResource(unfilledStarResId)
            }
            1 -> {
                binding.star1.setImageResource(unfilledStarResId)
                binding.star2.setImageResource(unfilledStarResId)
                binding.star3.setImageResource(unfilledStarResId)
                binding.star4.setImageResource(unfilledStarResId)
                binding.star5.setImageResource(unfilledStarResId)
            }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListReviewsItem>() {
            override fun areItemsTheSame(oldItem: ListReviewsItem, newItem: ListReviewsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListReviewsItem, newItem: ListReviewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}