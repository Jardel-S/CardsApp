package projeto.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import projeto.cards.database.CardDatabase
import projeto.cards.databinding.FragmentCardEditBinding
import java.util.concurrent.Executors

class CardEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var binding: FragmentCardEditBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var email: String? = null
    lateinit var card: Card
    lateinit var question: String
    lateinit var answer: String

    private val viewModel by lazy {
        ViewModelProvider(this).get(CardEditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_edit,
            container,
            false
        )

        firebaseAuth = FirebaseAuth.getInstance()

        val args = CardEditFragmentArgs.fromBundle(requireArguments())
        viewModel.loadCardId(args.cardId)

        viewModel.card.observe(viewLifecycleOwner) {
            card = it
            binding.card = card
            question = card.question
            answer = card.answer
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                card.question = s.toString()
            }
        }

        val answerTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                card.answer = s.toString()
            }
        }

        binding.questionEditText.addTextChangedListener(questionTextWatcher)
        binding.answerEditText.addTextChangedListener(answerTextWatcher)

        binding.cancelCardEditButton.setOnClickListener {
            card.question = question
            card.answer = answer
            executor.execute { CardDatabase.getInstance(requireContext()).cardDao.deleteCardId(card.id) }

            it.findNavController()
                .navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
        }

        binding.acceptCardEditButton.setOnClickListener {
            if (card.question == "" || card.answer == "") {
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.deleteCardId(
                        card.id
                    )
                }
            } else {
                executor.execute {
                    getAuthor()
                    card.author = email
                    CardDatabase.getInstance(requireContext()).cardDao.update(card)
                }
            }

            it.findNavController()
                .navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showAlertDialog()
                }
            })
    }

    private fun showAlertDialog() {}

    private fun getAuthor() {
        val firebaseUser = firebaseAuth.currentUser
        email = firebaseUser?.email
    }
}