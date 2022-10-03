import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import projeto.cards.Card
import projeto.cards.DeckWithCards
import projeto.cards.database.CardDatabase

class StatisticsViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    val decks: LiveData<List<DeckWithCards>> =
        CardDatabase.getInstance(context).cardDao.getDecksWithCards()

    private val deckSelected = MutableLiveData<String>()

    val deckWithCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(deckSelected) {
        CardDatabase.getInstance(context).cardDao.getDeckWithCards(it)
    }

    fun loadDeckId(id: String) {
        deckSelected.value = id
    }
}