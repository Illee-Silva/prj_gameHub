package com.drakarius.gamehub.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.drakarius.gamehub.databinding.ItemGameBinding
import com.drakarius.gamehub.model.Game

class GamesAdapter(private val games: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]

//        Log.d("IMAGE_DEBUG", "URL da imagem: ${game.background_image}")

        with(holder.binding) {
            tvGameName.text = game.name
            tvRatingNumber.text = game.rating.toString()
            tvReleaseDate.text = game.released
            ivGameImage.load(game.background_image){
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
            }
        }
    }

    override fun getItemCount() = games.size
}