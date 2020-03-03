package es.iessaladillo.pedrojoya.stroop.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.data.model.Game
import es.iessaladillo.pedrojoya.stroop.data.model.GameWithPlayer
import es.iessaladillo.pedrojoya.stroop.data.model.Player

class MainViewmodel(application: Application, private val database:AppDatabase) : ViewModel() {
    val settings:SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(application) }

    var gameToShow:GameWithPlayer? = null

    var playerCreatorTrigger:Boolean = true
    private var _newAvatar:MutableLiveData<Int> = MutableLiveData(0)
    val newAvatar:LiveData<Int> get() = _newAvatar

    private var _listPlayers:MutableLiveData<List<Player>> = MutableLiveData(database.playerDao.queryAllPlayers())
    val listPlayers:LiveData<List<Player>> get() = _listPlayers

    private var _listRanking:MutableLiveData<List<GameWithPlayer>> = MutableLiveData(getRankings(
        null
    )
    )
    val listRanking:LiveData<List<GameWithPlayer>> get() = _listRanking

    private var _selectedPlayer:MutableLiveData<Player?> = MutableLiveData(null)
    val selectedPlayer:LiveData<Player?> get() = _selectedPlayer

    private fun getRankings(selectedItem: String?):List<GameWithPlayer>{
        return if(selectedItem.isNullOrBlank()) filterGamesWithPlayer(settings.getString("gamemode","All")!!)
        else filterGamesWithPlayer(selectedItem)
    }

    private fun filterGamesWithPlayer(selector:String): List<GameWithPlayer> {
        return when (selector) {
            "Time" -> database.gameDao.queryGamesByTime()
            "Attempts" -> database.gameDao.queryGamesByAttmenpt()
            else -> database.gameDao.queryAllGames()
        }
    }

    fun createPlayer(player:Player){
        setSelectedPlayer(database.playerDao.queryPlayer(database.playerDao.insertPlayer(player)))
        updateListPlayers(null)
    }

    fun updatePlayer(player:Player){
        updateListPlayers(database.playerDao.updatePlayer(player))
        setSelectedPlayer(database.playerDao.queryPlayer(player.id))
    }

    fun deletePlayer(player:Player){
        setSelectedPlayer(null)
        updateListPlayers(database.playerDao.deletePlayer(player))
    }

    fun setSelectedPlayer(player: Player?) {
        _selectedPlayer.value = player
    }

    fun setCreationTrigger(bool: Boolean) {
        playerCreatorTrigger = bool
    }

    fun changeAvatar(avatar: Int) {
        _newAvatar.value = avatar
    }

    private fun updateListPlayers(result:Int?) {
        _listPlayers.value = database.playerDao.queryAllPlayers()
    }

    fun queryBy(selectedItem: String?) {
        getRankings(selectedItem)
    }

    fun createGame(game: Game) {
        database.gameDao.insertGame(game)
        gameToShow = database.gameDao.queryLastGame()
    }

    fun showGame(gameWithPlayer: GameWithPlayer){
        gameToShow = gameWithPlayer
    }
}
