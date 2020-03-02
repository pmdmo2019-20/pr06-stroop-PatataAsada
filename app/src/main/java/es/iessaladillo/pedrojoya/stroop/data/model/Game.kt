package es.iessaladillo.pedrojoya.stroop.data.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "game",
    foreignKeys = [
        ForeignKey(

            entity = Player::class,

            parentColumns = ["id"],

            childColumns = ["player_id"],

            onUpdate = CASCADE,

            onDelete = CASCADE

        )
    ]
)
data class Game(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "words")
    val words: Int,
    @ColumnInfo(name = "correct")
    val correct: Int,
    @ColumnInfo(name = "gamemode")
    val gamemode: String,
    @ColumnInfo(name = "time")
    val time: Int,
    @ColumnInfo(name = "player_id")
    val player_id: Long
) {
    val points: Int get() = correct * 10
}

data class GameWithPlayer(
    @Embedded
    val game:Game,
    @Relation(
        parentColumn = "player_id",
        entityColumn = "id"
    )
    val player:Player

)