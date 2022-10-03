package projeto.cards

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import projeto.cards.databinding.ListItemCardBinding

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    var data = listOf<Card>()
    lateinit var binding: ListItemCardBinding

    inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var local = binding
        fun bind(card: Card) {
            local.card = card

            itemView.setOnClickListener {
                it.findNavController()
                    .navigate(
                        CardListFragmentDirections.actionCardListFragmentToCardEditFragment(
                            card.id
                        )
                    )
            }

            local.expandBtn.setOnClickListener {
                if (local.expandableLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(local.cardView)
                    local.expandableLayout.visibility = View.VISIBLE
                    local.expandBtn.text = "COLLAPSE"
                } else {
                    TransitionManager.beginDelayedTransition(local.cardView, AutoTransition())
                    local.expandableLayout.visibility = View.GONE
                    local.expandBtn.text = "EXPAND"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemCardBinding.inflate(layoutInflater, parent, false)

        return CardHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(data[position])
    }
}