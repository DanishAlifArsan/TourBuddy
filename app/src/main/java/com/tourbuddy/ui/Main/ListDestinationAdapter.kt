package com.tourbuddy.ui.Main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tourbuddy.ui.detail.DetailActivity
import com.tourbuddy.R
import com.tourbuddy.api.response.ListDestinationsItem
import com.tourbuddy.databinding.DestinationListItemBinding

class ListDestinationAdapter() : ListAdapter<ListDestinationsItem, ListDestinationAdapter.ListViewHolder>(
    DIFF_CALLBACK
), Filterable{
    private var destinationList : List<ListDestinationsItem> = listOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = DestinationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(holder, destination)
    }

    class ListViewHolder (val binding: DestinationListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(holder : ListViewHolder, destination : ListDestinationsItem) {
            binding.tvItemName.text = destination.destinationName
            binding.tvLocation.text = destination.city
            binding.tvRating.text = destination.rating.toString()
            binding.tvReviewCount.text = holder.itemView.context.getString(R.string.review_count, destination.ratingCount.toString())
            Glide.with(holder.itemView.context)
                .load(destination.photoUrl)
                .into(binding.ivItemPhoto)

            val filledStarResId = R.drawable.star_enabled
            val unfilledStarResId = R.drawable.star_disable

            val stars : List<ImageView> = listOf(binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5)

            for (i in 1 .. destination.rating.toInt()) {
                stars[i-1].setImageResource(filledStarResId)
            }

            for (i in (destination.rating.toInt() + 1)..stars.size) {
                stars[i - 1].setImageResource(unfilledStarResId)
            }

            holder.itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra("key_destination", destination)
                holder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListDestinationsItem>() {
            override fun areItemsTheSame(oldItem: ListDestinationsItem, newItem: ListDestinationsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListDestinationsItem, newItem: ListDestinationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                        destinationList
                    else onFilter(destinationList, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                submitList(result?.values as? List<ListDestinationsItem>)
            }
        }
    }

    fun onFilter(list : List<ListDestinationsItem>, constraint : String) : List<ListDestinationsItem> {
        val filteredList = list.filter {
            it.destinationName.lowercase().contains(constraint.lowercase()) ||
                    it.city.lowercase().contains(constraint.lowercase())
        }
        return filteredList
    }

    fun addToList(_destinationList : List<ListDestinationsItem>) {
        destinationList = _destinationList
        submitList(destinationList)
    }
}