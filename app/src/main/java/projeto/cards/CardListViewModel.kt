package projeto.cards

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import projeto.cards.database.CardDatabase

class CardListViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser = firebaseAuth.currentUser
    private var email: String? = firebaseUser?.email

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val cards: LiveData<List<Card>>? = email?.let {
        CardDatabase.getInstance(context).cardDao.getCardsFromAuth(
            it
        )
    }
}