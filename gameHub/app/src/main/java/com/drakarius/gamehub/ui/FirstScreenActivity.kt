package com.drakarius.gamehub.ui

import android.content.Intent
import android.os.Bundle
import android.app.AlertDialog
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.drakarius.gamehub.R
import com.drakarius.gamehub.databinding.ActivityFirstScreenBinding

class FirstScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logoImageView.load(R.drawable.chatgpt_image_24_de_mai__de_2025__21_27_33) {
            transformations(CircleCropTransformation())
        }

        binding.popularGamesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.searchButton.setOnClickListener {
            val editText = EditText(this)
            editText.hint = "Digite o nome do jogo"
            AlertDialog.Builder(this)
                .setTitle("Pesquisar jogo")
                .setView(editText)
                .setPositiveButton("Pesquisar") { _, _ ->
                    val query = editText.text.toString().trim()
                    if (query.isNotEmpty()) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("search_query", query)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
