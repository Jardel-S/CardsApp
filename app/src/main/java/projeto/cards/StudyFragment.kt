package projeto.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import projeto.cards.databinding.FragmentStudyBinding

class StudyFragment : Fragment() {
    lateinit var binding: FragmentStudyBinding

    val viewModel: StudyViewModel by lazy {
        ViewModelProvider(this).get(StudyViewModel::class.java)
    }

    private var listener = View.OnClickListener { v ->

        val quality = when (v?.id) {
            R.id.hardButton -> 0
            R.id.doubtButton -> 3
            R.id.easyButton -> 5
            else -> throw Exception("Invalid quality")
        }
        viewModel.update(quality)

        if (viewModel.card == null) {
            Toast.makeText(context, "No more cards to review", Toast.LENGTH_SHORT).show()
            viewModel.card?.no_cards = true
        }
        binding.invalidateAll()
    }

    override fun onStart() {
        super.onStart()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentStudyBinding>(
            inflater,
            R.layout.fragment_study,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.answerButton.setOnClickListener {
            viewModel.card?.answered = true
            binding.invalidateAll()
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val board = prefs.getBoolean("board", false)

        binding.apply {
            drawerLayout?.isVisible = board
        }

        viewModel.dueCard.observe(viewLifecycleOwner) {
            viewModel.card = it
            binding.invalidateAll()
        }

        binding.hardButton.setOnClickListener(listener)
        binding.doubtButton.setOnClickListener(listener)
        binding.easyButton.setOnClickListener(listener)

        return binding.root
    }
}