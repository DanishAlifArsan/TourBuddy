package com.tourbuddy.api

import com.google.gson.annotations.SerializedName

data class ListReviewResponse(

	@field:SerializedName("listReviews")
	val listReviews: ArrayList<ListReviewsItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListReviewsItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("reviewer_name")
	val reviewerName: String,

	@field:SerializedName("review")
	val review: String,

	@field:SerializedName("rating")
	val rating: Int
)
