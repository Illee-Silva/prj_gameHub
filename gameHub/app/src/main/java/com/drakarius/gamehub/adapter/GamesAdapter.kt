package com.drakarius.gamehub.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.drakarius.gamehub.databinding.ItemGameBinding
import com.drakarius.gamehub.model.Game

class GamesAdapter(
    private val games: MutableList<Game>,
    private val onItemClick: (Game) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(games[position])
                }
            }
        }
    }

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
            tvEsrbRating.text = game.esrb_rating?.name
            ivGameImage.load(game.background_image){
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                transformations(RoundedCornersTransformation(16f))
            }
        }
    }

    override fun getItemCount() = games.size

    fun setGames(newGames: List<Game>) {
        games.clear()
        games.addAll(newGames)
        notifyDataSetChanged()
    }

    fun addGames(newGames: List<Game>) {
       val startPosition = games.size
        games.addAll(newGames)
        notifyItemRangeInserted(startPosition, newGames.size)
    }

    fun clearGames() {
        games.clear()
        notifyDataSetChanged()
    }

}
