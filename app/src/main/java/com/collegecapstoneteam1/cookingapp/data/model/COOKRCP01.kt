package com.collegecapstoneteam1.cookingapp.data.model


import com.google.gson.annotations.SerializedName

data class COOKRCP01(
    @SerializedName("RESULT")
    val rESULT: RESULT,
    @SerializedName("row")
    val recipes: List<Recipe>,
    @SerializedName("total_count")
    val totalCount: String
)