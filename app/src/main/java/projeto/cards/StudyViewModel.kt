package projeto.cards

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import projeto.cards.database.CardDatabase
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val executor = Executors.newSingleThreadExecutor()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var firebaseAuth = FirebaseAuth.getInstance()

    var card: Card? = null
    val firebaseUser = firebaseAuth.currentUser
    private var email: String? = firebaseUser!!.email

    var cards: LiveData<List<Card>> =
        CardDatabase.getInstance(context).cardDao.getCardsFromAuth(email.toString())
    var dueCard: LiveData<Card?> = Transformations.map(cards) {
        try {
            it.filter {
                it.isdue(LocalDateTime.now())
            }.random()
        } catch (e: Exception) {
            null
        }
    }
    var nDueCards: LiveData<Int> = Transformations.map(cards) {
        it.filter {
            it.isdue(LocalDateTime.now())
        }.size
    }

    fun update(quality: Int) {
        card?.quality = quality
        card?.update(LocalDateTime.now())
        executor.execute {
            CardDatabase.getInstance(context).cardDao.update(card!!)
        }
    }
}
