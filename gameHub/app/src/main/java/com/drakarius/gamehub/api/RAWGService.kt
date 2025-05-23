package com.drakarius.gamehub.api

import com.drakarius.gamehub.model.GameResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RAWGService {

    @GET("games")
    suspend fun getGames(
        @Query("key") apikey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 50,
    ): GameResponse

}