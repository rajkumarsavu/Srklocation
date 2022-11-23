package com.srk.srklocationservices.models.placedetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.srk.srklocationservices.models.common.AddressComponent
import com.srk.srklocationservices.models.common.Geometry
import com.srk.srklocationservices.models.common.Photo

class PlaceResult {
    @SerializedName("address_components")
    @Expose
    var addressComponents: List<AddressComponent>? = null

    @SerializedName("adr_address")
    @Expose
    var adrAddress: String? = null

    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String? = null

    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null

    @SerializedName("icon")
    @Expose
    var icon: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("photos")
    @Expose
    var photos: List<Photo>? = null

    @SerializedName("place_id")
    @Expose
    var placeId: String? = null

    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("scope")
    @Expose
    var scope: String? = null

    @SerializedName("types")
    @Expose
    var types: List<String>? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("utc_offset")
    @Expose
    var utcOffset: String? = null

    @SerializedName("vicinity")
    @Expose
    var vicinity: String? = null
}
