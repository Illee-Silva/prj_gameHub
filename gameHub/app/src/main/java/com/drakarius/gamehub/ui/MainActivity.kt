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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: GamesAdapter
    private var isLoading = true
    private var currentPage = 1
    private val pageSize = 40

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
        adapter = GamesAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        binding.recyclerView.apply{
            setLayoutManager(LinearLayoutManager(this@MainActivity))

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager!!.itemCount
                    val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    Log.d("SCROLL_DEBUG", "Visible items: $visibleItemCount, Total items: $totalItemCount, First visible item: $firstVisibleItemPosition")

                    if(isLoading == false){
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize)
                        {
                            loadMoreData()
                        }
                    }

                }
            })

        }

    }

    private fun loadMoreData(){
        if (isLoading) return

        isLoading = true

        viewModel.loadGames("3b861da78cde4e23a99ef4a3c20a12d5", currentPage)

    }

    private fun observeData() {
        viewModel.games.observe(this) { games ->
            if(currentPage == 1){
                adapter.setGames(games)
                Log.d("GAMES_DEBUG", "Games loaded: ${games.size}")
            }
            else{
                adapter.addGames(games)
                Log.d("GAMES_DEBUG", "Games ADDED: ${games.size}")
            }
            currentPage++
            isLoading = false
        }
    }


}
