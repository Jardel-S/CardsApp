package projeto.cards.database

import androidx.lifecycle.LiveData
import androidx.room.*
import projeto.cards.Card
import projeto.cards.Deck
import projeto.cards.DeckWithCards

@Dao
interface CardDao {
    // Card methods
    @Query("SELECT * FROM cards_table")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards_table WHERE author = :email")
    fun getCardsFromAuth(email: String): LiveData<List<Card>>

    @Query("SELECT * FROM cards_table WHERE id = :id")
    fun getCard(id: String): LiveData<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Card)

    @Update
    fun update(card: Card)

    @Query("DELETE FROM cards_table WHERE id = :id")
    fun deleteCardId(id: String)

    @Query("DELETE FROM cards_table WHERE deckId = :id")
    fun deleteCard(id: String)

    // Decks methods
    @Query("SELECT * FROM decks_table")
    fun getDecks(): LiveData<List<Deck>>

    @Query("SELECT * FROM decks_table WHERE author = :email")
    fun getDecksFromAuth(email: String): LiveData<List<Deck>>

    @Query("SELECT * FROM decks_table WHERE deckId = :id")
    fun getDeck(id: String): LiveData<Deck?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Query("SELECT deckId FROM decks_table WHERE deck_name = :name")
    fun getDeckId(name: String): String

    @Query("DELETE FROM decks_table WHERE deckId = :id")
    fun deleteDeckId(id: String)

    // Card DecksWithCards methods
    @Transaction
    @Query("SELECT * FROM decks_table")
    fun getDecksWithCards(): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM decks_table WHERE deckId = :deckId")
    fun getDeckWithCards(deckId: String): LiveData<List<DeckWithCards>>
}