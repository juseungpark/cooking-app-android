package com.collegecapstoneteam1.cookingapp.data.api

import com.collegecapstoneteam1.cookingapp.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeSearchApi {
    @GET("/COOKRCP01/json/{startIdx}/{endIdx}")
    suspend fun searchRecipes(
        @Path("startIdx") startIdx: Int,
        @Path("endIdx") endIdx: Int,
        @Query("RCP_NM") recipeName: String,
        @Query("RCP_PARTS_DTLS") recipeDetail: String
    ): Response<SearchResponse>
}