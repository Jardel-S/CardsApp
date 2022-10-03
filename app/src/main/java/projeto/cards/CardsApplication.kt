package projeto.cards

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors

class CardsApplication : Application() {

    private var _nDueCards = MutableLiveData<Int>()
    private val executor = Executors.newSingleThreadExecutor()

    val nDueCards: LiveData<Int>
        get() = _nDueCards

    companion object {
        fun numberOfDueCards() = 5
    }
}