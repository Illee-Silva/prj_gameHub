package com.drakarius.gamehub.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakarius.gamehub.adapter.GamesAdapter
import com.drakarius.gamehub.databinding.ActivityMainBinding
import com.drakarius.gamehub.model.Game
import kotlinx.coroutines.delay
import com.drakarius.gamehub.model.Screenshot
import android.app.AlertDialog
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: GamesAdapter
    private var isLoading = true
    private var currentPage = 1
    private val pageSize = 40
    private var allGames: List<Game> = emptyList()
    private var currentFilterRating: Double? = null
    private var currentSearchQuery: String? = null
    private var currentOrdering: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentSearchQuery = intent.getStringExtra("search_query")
        currentOrdering = null // Não ordena por rating inicialmente
        currentPage = 1
        allGames = emptyList()
        isLoading = true

        setupRecyclerView()
        observeData()
        loadGamesWithCurrentFilters(reset = true)
    }

    private fun setupRecyclerView() {
        adapter = GamesAdapter(mutableListOf()) { game ->
            val intent = android.content.Intent(this, GameDetailActivity::class.java)
            intent.putExtra("game_id", game.id)
            intent.putExtra("game_name", game.name)
            intent.putExtra("game_image", game.background_image)
            val screenshots = ArrayList<String>(game.short_screenshots.map { it.image })
            intent.putStringArrayListExtra("screenshots", screenshots)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.orderByRatingButton.setOnClickListener {
            // Alterna entre ordenar por rating e ordem padrão
            currentOrdering = if (currentOrdering == "-rating") null else "-rating"
            currentPage = 1
            allGames = emptyList()
            loadGamesWithCurrentFilters(reset = true)
        }


        binding.recyclerView.apply {
            setLayoutManager(LinearLayoutManager(this@MainActivity))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager!!.itemCount
                    val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (!isLoading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize
                        ) {
                            loadGamesWithCurrentFilters(reset = false)
                        }
                    }
                }
            })
        }
    }

    private fun loadGamesWithCurrentFilters(reset: Boolean) {
        isLoading = true
        if (reset) {
            currentPage = 1
            allGames = emptyList()
        }
        viewModel.loadGames(
            "3b861da78cde4e23a99ef4a3c20a12d5",
            page = currentPage,
            search = currentSearchQuery,
            ordering = currentOrdering
        )
    }

    private fun filterAndShowGames() {
        val filtered = if (currentFilterRating != null) {
            allGames.filter { it.rating >= currentFilterRating!! }
        } else {
            allGames
        }
        adapter.setGames(filtered)
    }

    private fun loadMoreData() {
        loadGamesWithCurrentFilters(reset = false)
    }

    private fun observeData() {
        viewModel.games.observe(this) { games ->
            if (currentPage == 1) {
                allGames = games
                adapter.setGames(games)
            } else {
                allGames = allGames + games
                adapter.addGames(games)
            }
            Log.d("GAMES_DEBUG", "Games loaded: ${games.size} (page $currentPage)")
            currentPage++
            isLoading = false
        }
    }
}
