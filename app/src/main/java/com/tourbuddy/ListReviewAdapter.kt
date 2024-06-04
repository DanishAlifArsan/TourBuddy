package com.tourbuddy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tourbuddy.api.ListReviewResponse
import com.tourbuddy.api.ListReviewsItem
import com.tourbuddy.data.Review

class ListReviewAdapter(private val listReview: ArrayList<ListReviewsItem>) : RecyclerView.Adapter<ListReviewAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.review_list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listReview.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (date, name , review, rating)= listReview[position]

        holder.imgPhoto.setImageResource(R.drawable.ic_face)
        holder.tvName.text = name
        holder.tvReview.text = review
        holder.tvRating.text = rating.toString()
        holder.tvDate.text = date

        val filledStarResId = R.drawable.star_enabled
        val emptyStarResId = R.drawable.star_disable
        when(rating){
            5 -> {
                holder.ivStar1.setImageResource(filledStarResId)
                holder.ivStar2.setImageResource(filledStarResId)
                holder.ivStar3.setImageResource(filledStarResId)
                holder.ivStar4.setImageResource(filledStarResId)
                holder.ivStar5.setImageResource(filledStarResId)
            }
            4 -> {
                holder.ivStar1.setImageResource(filledStarResId)
                holder.ivStar2.setImageResource(filledStarResId)
                holder.ivStar3.setImageResource(filledStarResId)
                holder.ivStar4.setImageResource(filledStarResId)
                holder.ivStar5.setImageResource(emptyStarResId)
            }
            3 -> {
                holder.ivStar1.setImageResource(filledStarResId)
                holder.ivStar2.setImageResource(filledStarResId)
                holder.ivStar3.setImageResource(filledStarResId)
                holder.ivStar4.setImageResource(emptyStarResId)
                holder.ivStar5.setImageResource(emptyStarResId)
            }
            2 -> {
                holder.ivStar1.setImageResource(filledStarResId)
                holder.ivStar2.setImageResource(filledStarResId)
                holder.ivStar3.setImageResource(emptyStarResId)
                holder.ivStar4.setImageResource(emptyStarResId)
                holder.ivStar5.setImageResource(emptyStarResId)
            }
            1 -> {
                holder.ivStar1.setImageResource(filledStarResId)
                holder.ivStar2.setImageResource(emptyStarResId)
                holder.ivStar3.setImageResource(emptyStarResId)
                holder.ivStar4.setImageResource(emptyStarResId)
                holder.ivStar5.setImageResource(emptyStarResId)
            }
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvReview: TextView = itemView.findViewById(R.id.tv_review)
        val tvRating: TextView = itemView.findViewById(R.id.tv_rating)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val ivStar1: ImageView = itemView.findViewById(R.id.star1)
        val ivStar2: ImageView = itemView.findViewById(R.id.star2)
        val ivStar3: ImageView = itemView.findViewById(R.id.star3)
        val ivStar4: ImageView = itemView.findViewById(R.id.star4)
        val ivStar5: ImageView = itemView.findViewById(R.id.star5)
    }
}