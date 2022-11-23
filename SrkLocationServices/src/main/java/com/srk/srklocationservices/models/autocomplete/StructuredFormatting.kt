package com.srk.srklocationservices.models.autocomplete

import com.google.gson.annotations.SerializedName

data class StructuredFormatting (

  @SerializedName("main_text"                    ) var mainText                  : String?                              = null,
  @SerializedName("main_text_matched_substrings" ) var mainTextMatchedSubstrings : ArrayList<MainTextMatchedSubstrings> = arrayListOf(),
  @SerializedName("secondary_text"               ) var secondaryText             : String?                              = null

)