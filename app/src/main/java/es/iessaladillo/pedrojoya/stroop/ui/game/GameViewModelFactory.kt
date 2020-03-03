package es.iessaladillo.pedrojoya.stroop.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel

class GameViewModelFactory(
    private val time: Int,
    private val wordTime: Int,
    private val gamemode: String,
    private val attempts: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(time, wordTime, gamemode, attempts) as T
        }
        throw IllegalArgumentException("Must provide MainViewmodel class")
    }

}