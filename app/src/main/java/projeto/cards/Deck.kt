package projeto.cards

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "decks_table")
open class Deck(
    @ColumnInfo(name = "deck_name")
    var name: String,
    var author: String? = null,
    @PrimaryKey
    val deckId: String = UUID.randomUUID().toString()
)