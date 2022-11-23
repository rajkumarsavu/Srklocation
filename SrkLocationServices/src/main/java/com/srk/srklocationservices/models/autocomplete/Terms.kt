package com.srk.srklocationservices.models.autocomplete

import com.google.gson.annotations.SerializedName

data class Terms(

    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("value") var value: String? = null

)