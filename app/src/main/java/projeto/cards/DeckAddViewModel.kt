package projeto.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import projeto.cards.database.CardDatabase

class DeckAddViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val deckId = MutableLiveData<String>()

    val deck: LiveData<Deck> = Transformations.switchMap(deckId) {
        CardDatabase.getInstance(context).cardDao.getDeck(it)
    }

    fun loadDeckId(id: String) {
        deckId.value = id
    }
}