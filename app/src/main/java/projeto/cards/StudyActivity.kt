package projeto.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import projeto.cards.databinding.ActivityStudyBinding

class StudyActivity : AppCompatActivity() {

    lateinit var binding: ActivityStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_study)
    }
}