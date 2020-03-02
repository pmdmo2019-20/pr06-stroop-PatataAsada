package es.iessaladillo.pedrojoya.stroop.data.dao

import androidx.room.*
import es.iessaladillo.pedrojoya.stroop.data.model.Player

@Dao
interface PlayerDAO {

    @Query("SELECT * FROM player")
    fun queryAllPlayers(): List<Player>

    @Query("SELECT * FROM player WHERE id = :playerId")
    fun queryPlayer(playerId:Long):Player

    @Insert
    fun insertPlayer(player: Player): Long

    @Update
    fun updatePlayer(player: Player): Int

    @Delete
    fun deletePlayer(player: Player): Int
}