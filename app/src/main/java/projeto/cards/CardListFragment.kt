package projeto.cards

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import projeto.cards.database.CardDatabase
import projeto.cards.databinding.FragmentCardListBinding
import java.util.concurrent.Executors

class CardListFragment : Fragment() {
    private lateinit var adapter: CardAdapter
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var binding: FragmentCardListBinding

    private val cardListViewModel by lazy {
        ViewModelProvider(this).get(CardListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
            R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()
                findNavController()
                    .navigate(R.id.action_cardListFragment_to_titleFragment)
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentCardListBinding>(
            inflater,
            R.layout.fragment_card_list,
            container,
            false
        )

        SettingsActivity.setLoggedIn(requireContext(), true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val max_number_cards = prefs.getString("max_number_cards", "")

        Snackbar.make(
            binding.cardListRecyclerView,
            "Max number of cards is: $max_number_cards)}",
            Snackbar.LENGTH_LONG
        ).show()
        //Snackbar.make(binding.cardListRecyclerView,"Max number of cards is: $board", Snackbar.LENGTH_LONG).show()

        adapter = CardAdapter()
        adapter.data = emptyList()
        binding.cardListRecyclerView.adapter = adapter
        val args = CardListFragmentArgs.fromBundle(requireArguments())

        binding.newCardFab.setOnClickListener {
            val card = Card("", "", deckId = args.deckId)

            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addCard(card)
            }

            it.findNavController()
                .navigate(CardListFragmentDirections.actionCardListFragmentToCardEditFragment(card.id))
        }

        cardListViewModel.cards?.observe(viewLifecycleOwner) {
            adapter.data = it.filter { carOfDeck -> carOfDeck.deckId == args.deckId }
            adapter.notifyDataSetChanged()
        }

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.itemId
                var id = adapter.data.get(viewHolder.absoluteAdapterPosition).id
                showDialog(id)
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.cardListRecyclerView)

        return binding.root
    }

    private fun showDialog(cardId: String) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Delete Deck")
        builder?.setMessage("Are you sure you want to delete this deck?")
        builder?.setPositiveButton("Confirm") { _, _ ->
            cardListViewModel.cards?.observe(viewLifecycleOwner) {
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.deleteCardId(cardId)
                }
                adapter.notifyDataSetChanged()
            }
        }
        builder?.setNegativeButton("Cancel") { _, _ ->

            adapter.notifyDataSetChanged()
        }
        builder?.show()
    }

}
