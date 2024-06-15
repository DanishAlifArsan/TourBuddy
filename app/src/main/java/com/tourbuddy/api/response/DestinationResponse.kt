package com.tourbuddy.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DestinationResponse(

	@field:SerializedName("listDestinations")
	val listDestinations: ArrayList<ListDestinationsItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class ListDestinationsItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("destination_id")
	val destinationId: String,

	@field:SerializedName("eco_Rating")
	val ecoRating: Float,

	@field:SerializedName("rating")
	val userRating: Float,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("destination_name")
	val destinationName: String,

	@field:SerializedName("url_maps")
	val urlMaps: String,

	@field:SerializedName("rating_count")
	val ratingCount: Int
) : Parcelable
