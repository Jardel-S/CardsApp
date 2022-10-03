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
import projeto.cards.databinding.FragmentDeckAddBinding
import java.util.concurrent.Executors

class DeckAddFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var binding: FragmentDeckAddBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var email: String? = null
    lateinit var deck: Deck
    lateinit var name: String

    private val deckAddViewModel by lazy {
        ViewModelProvider(this).get(DeckAddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_deck_add,
            container,
            false
        )

        firebaseAuth = FirebaseAuth.getInstance()
        val args = DeckAddFragmentArgs.fromBundle(requireArguments())

        deckAddViewModel.loadDeckId(args.deckID)

        deckAddViewModel.deck.observe(viewLifecycleOwner) {
            deck = it
            binding.deck = deck
            name = deck.name
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val deckname = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                deck.name = s.toString()
            }
        }

        binding.createDeck.addTextChangedListener(deckname)

        binding.cancelButton.setOnClickListener {
            executor.execute { CardDatabase.getInstance(requireContext()).cardDao.deleteDeckId(deck.deckId) }
            it.findNavController()
                .navigate(
                    DeckAddFragmentDirections.actionDeckAddFragmentToDeckListFragment()
                )
        }

        binding.okButton.setOnClickListener {
            if (deck.name == "") {
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.deleteDeckId(
                        deck.deckId
                    )
                }
            } else {
                executor.execute {
                    getAuthor()
                    deck.author = email
                    CardDatabase.getInstance(requireContext()).cardDao.addDeck(deck)
                }
            }

            it.findNavController()
                .navigate(
                    DeckAddFragmentDirections.actionDeckAddFragmentToDeckListFragment()
                )
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

    private fun showAlertDialog() {

    }

    private fun getAuthor() {
        val firebaseUser = firebaseAuth.currentUser
        email = firebaseUser?.email
    }
}