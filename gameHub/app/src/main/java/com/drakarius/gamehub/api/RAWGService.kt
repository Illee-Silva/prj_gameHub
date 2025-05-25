package com.drakarius.gamehub.api

import com.drakarius.gamehub.model.GameResponse
import com.drakarius.gamehub.model.Game
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface RAWGService {

    @GET("games")
    suspend fun getGames(
        @Query("key") apikey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 40,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("rating") rating: String? = null
    ): GameResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") apikey: String
    ): Game
}

