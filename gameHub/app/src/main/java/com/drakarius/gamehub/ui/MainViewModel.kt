package com.drakarius.gamehub.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drakarius.gamehub.api.RetrofitClient
import com.drakarius.gamehub.model.Game
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: MutableLiveData<List<Game>> = _games

    fun loadGames(apiKey: String) {

        viewModelScope.launch{
            try{
                val response = RetrofitClient.service.getGames(apiKey)
                _games.postValue(response.results)
            }
            catch (e: Exception) {
                e.printStackTrace()
                _games.postValue(emptyList())
            }
        }

    }

}