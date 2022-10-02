package com.collegecapstoneteam1.cookingapp.data.model


import com.google.gson.annotations.SerializedName

data class RESULT(
    @SerializedName("CODE")
    val cODE: String,
    @SerializedName("MSG")
    val mSG: String
)