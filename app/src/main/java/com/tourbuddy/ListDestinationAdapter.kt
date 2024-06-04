package com.tourbuddy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tourbuddy.api.ListDestinationsItem

class ListDestinationAdapter(private var listDestination: List<ListDestinationsItem>): RecyclerView.Adapter<ListDestinationAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.destination_list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listDestination.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, city, id, rating, description, name, location, ratingCount) = listDestination[position]

        holder.tvName.text = name
        holder.tvlocation.text = city
        holder.tvRating.text = rating.toString()
        holder.tvReviewCount.text = holder.itemView.context.getString(R.string.review_count, ratingCount.toString())
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)

        val filledStarResId = R.drawable.star_enabled

        for (i in 1 .. rating.toInt()) {
            holder.stars[i-1].setImageResource(filledStarResId)
        }

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("key_destination", listDestination[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    fun setFilteredList(filteredDestination : List<ListDestinationsItem>) {
        listDestination = filteredDestination
        notifyDataSetChanged()

    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvlocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvRating : TextView = itemView.findViewById(R.id.tv_rating)
        val tvReviewCount : TextView = itemView.findViewById(R.id.tv_review_count)
        val stars : List<ImageView> = listOf(itemView.findViewById(R.id.star1),
            itemView.findViewById(R.id.star2),
            itemView.findViewById(R.id.star3),
            itemView.findViewById(R.id.star4),
            itemView.findViewById(R.id.star5))
    }
}