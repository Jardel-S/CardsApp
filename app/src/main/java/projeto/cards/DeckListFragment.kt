package projeto.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import projeto.cards.database.CardDatabase
import projeto.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors


class DeckListFragment : Fragment() {
    private lateinit var adapter: DeckAdapter
    lateinit var binding: FragmentDeckListBinding
    private val executor = Executors.newSingleThreadExecutor()
    private var email: String? = null

    private val deckListViewModel by lazy {
        ViewModelProvider(this).get(DeckListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentDeckListBinding>(
            inflater,
            R.layout.fragment_deck_list,
            container,
            false
        )

        adapter = DeckAdapter()
        adapter.data = emptyList()
        binding.deckListRecyclerView.adapter = adapter

        /*Create deck*/
        binding.newDeckFab.setOnClickListener {
            val deck = Deck("")

            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addDeck(deck)
            }

            it.findNavController()
                .navigate(
                    DeckListFragmentDirections.actionDeckListFragmentToDeckAddFragment(deck.deckId)
                )
        }

        deckListViewModel.decks?.observe(viewLifecycleOwner) {
            adapter.data = it
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
                var id = adapter.data.get(viewHolder.absoluteAdapterPosition).deckId
                showDialog(viewHolder, id)
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(binding.deckListRecyclerView)

        return binding.root
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder, deckId: String) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Delete Deck")
        builder?.setMessage("Are you sure you want to delete this deck?")
        builder?.setPositiveButton("Confirm") { _, _ ->
            val position = viewHolder.bindingAdapterPosition
            deckListViewModel.decks?.observe(viewLifecycleOwner) {
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.deleteDeckId(deckId)
                    CardDatabase.getInstance(requireContext()).cardDao.deleteCard(deckId)
                }

                //Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT)
                adapter.notifyDataSetChanged()
            }
        }
        builder?.setNegativeButton("Cancel") { _, _ ->

            adapter.notifyDataSetChanged()
            //Toast.makeText(activity, "No", Toast.LENGTH_SHORT)
        }
        builder?.show()
    }

}