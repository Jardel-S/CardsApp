package projeto.cards

import androidx.room.Embedded
import androidx.room.Relation

class DeckWithCards(
    @Embedded
    val deck: Deck,
    @Relation(parentColumn = "deckId", entityColumn = "deckId")
    val cards: List<Card>
)