package com.drakarius.gamehub.model

data class Game(
    val id: Int,
    val name: String,
    val description: String,
    val released: String,
    val rating: Double,
    val ratingCount: Int,
    val background_image: String?
)

data class GameResponse(
    val results: List<Game>
)