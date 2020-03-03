package es.iessaladillo.pedrojoya.stroop.data.dao

import androidx.room.*
import es.iessaladillo.pedrojoya.stroop.data.model.Game
import es.iessaladillo.pedrojoya.stroop.data.model.GameWithPlayer

@Dao
interface GameDAO {

    @Query("SELECT * FROM game ORDER BY correct DESC")
    fun queryAllGames():List<GameWithPlayer>

    @Query("SELECT * FROM game WHERE gamemode='Time' ORDER BY correct DESC")
    fun queryGamesByTime():List<GameWithPlayer>

    @Query("SELECT * FROM game WHERE gamemode='Attempts' ORDER BY correct DESC")
    fun queryGamesByAttmenpt():List<GameWithPlayer>

    @Insert
    fun insertGame(game:Game)

    @Update
    fun updateGame(game:Game)

    @Delete
    fun deleteGame(game:Game)

    @Query("SELECT * FROM game ORDER BY id ASC LIMIT 1")
    fun queryLastGame(): GameWithPlayer
}