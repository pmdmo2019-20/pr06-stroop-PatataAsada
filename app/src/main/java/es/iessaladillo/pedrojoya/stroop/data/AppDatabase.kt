package es.iessaladillo.pedrojoya.stroop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDAO
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDAO
import es.iessaladillo.pedrojoya.stroop.data.model.Game
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import kotlin.concurrent.thread

@Database(entities = [Player::class,Game::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract val playerDao:PlayerDAO
    abstract val gameDao:GameDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}