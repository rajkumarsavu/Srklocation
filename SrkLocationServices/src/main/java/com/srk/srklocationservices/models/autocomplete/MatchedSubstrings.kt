package com.srk.srklocationservices.models.autocomplete

import com.google.gson.annotations.SerializedName

data class MatchedSubstrings(
    @SerializedName("length") var length: Int? = null,
    @SerializedName("offset") var offset: Int? = null
)