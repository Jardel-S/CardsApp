package projeto.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import projeto.cards.databinding.ActivityEmailPasswordBinding
import projeto.cards.databinding.ActivityTitleBinding

class EmailPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmailPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_password)
    }
}