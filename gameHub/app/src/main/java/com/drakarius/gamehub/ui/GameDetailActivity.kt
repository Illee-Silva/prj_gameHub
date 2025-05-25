package com.drakarius.gamehub.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.drakarius.gamehub.api.RetrofitClient
import com.drakarius.gamehub.databinding.ActivityGameDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakarius.gamehub.adapter.ScreenshotAdapter

class GameDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameId = intent.getIntExtra("game_id", -1)
        val gameName = intent.getStringExtra("game_name")
        val gameImage = intent.getStringExtra("game_image")
        val screenshots = intent.getStringArrayListExtra("screenshots") ?: arrayListOf()
        binding.tvGameName.text = gameName ?: "Sem nome"
        binding.tvGameDescription.text = "Carregando descrição..."

        binding.ivGameImage.load(gameImage) {
            placeholder(android.R.drawable.ic_menu_gallery)
            crossfade(true)
        }

        // Remove a primeira screenshot (que é igual à capa) antes de exibir
        val screenshotsToShow = if (screenshots.isNotEmpty()) screenshots.drop(1) else screenshots
        binding.rvScreenshots.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvScreenshots.adapter = ScreenshotAdapter(screenshotsToShow.mapIndexed { idx, url -> com.drakarius.gamehub.model.Screenshot(idx, url) })

        if (gameId != -1) {
            lifecycleScope.launch {
                try {
                    val game = withContext(Dispatchers.IO) {
                        RetrofitClient.service.getGameDetail(gameId, "3b861da78cde4e23a99ef4a3c20a12d5")
                    }
                    binding.tvGameDescription.text = HtmlCompat.fromHtml(game.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                } catch (e: Exception) {
                    binding.tvGameDescription.text = "Erro ao carregar descrição."
                    Toast.makeText(this@GameDetailActivity, "Erro ao buscar detalhes do jogo", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.tvGameDescription.text = "ID do jogo inválido."
        }
    }
}
