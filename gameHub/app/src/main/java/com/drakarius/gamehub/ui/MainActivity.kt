package com.drakarius.gamehub.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakarius.gamehub.adapter.GamesAdapter
import com.drakarius.gamehub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeData()
        viewModel.loadGames("3b861da78cde4e23a99ef4a3c20a12d5")

    }

    private fun setupRecyclerView() {
        adapter = GamesAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.games.observe(this) { games ->
            adapter = GamesAdapter(games)
            binding.recyclerView.adapter = adapter
        }
    }


}