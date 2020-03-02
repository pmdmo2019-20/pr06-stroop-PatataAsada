package es.iessaladillo.pedrojoya.stroop.ui

import android.app.Application
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.avatars
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.data.model.Avatar
import es.iessaladillo.pedrojoya.stroop.data.model.GameWithPlayer
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import kotlin.concurrent.thread

class MainViewmodel(application: Application, private val database:AppDatabase) : ViewModel() {
    private val settings:SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(application) }

    var playerCreatorTrigger:Boolean = true
    private var _newPlayer:MutableLiveData<Avatar> = MutableLiveData(Avatar(0))
    val newPlayer:LiveData<Avatar> get() = _newPlayer

    private var _listPlayers:MutableLiveData<List<Player>> = MutableLiveData(database.playerDao.queryAllPlayers())
    val listPlayers:LiveData<List<Player>> get() = _listPlayers

    private var _listRanking:MutableLiveData<List<GameWithPlayer>> = MutableLiveData(getRankings())
    val listRanking:LiveData<List<GameWithPlayer>> get() = _listRanking

    private var _selectedPlayer:MutableLiveData<Player?> = MutableLiveData(null)
    val selectedPlayer:LiveData<Player?> get() = _selectedPlayer

    private fun getRankings():List<GameWithPlayer>{
        return when (settings.getString("gamemode","All")) {
            "Time" -> database.gameDao.queryGamesByTime()
            "Attempts" -> database.gameDao.queryGamesByAttmenpt()
            else -> database.gameDao.queryAllGames()
        }
    }

    fun createPlayer(player:Player){
        thread { database.playerDao.insertPlayer(player) }
    }

    fun updatePlayer(player:Player){
        thread { database.playerDao.updatePlayer(player) }
    }

    fun deletePlayer(player:Player){
        thread { database.playerDao.deletePlayer(player) }
    }

    fun setSelectedPlayer(player: Player) {
        _selectedPlayer.value = player
    }

    fun setCreationTrigger(bool: Boolean) {
        playerCreatorTrigger = bool
    }

    fun changeAvatar(avatar: Avatar) {
        if(playerCreatorTrigger) _newPlayer.value = avatar
    }
}