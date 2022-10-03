package projeto.cards

import StatisticsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import projeto.cards.databinding.FragmentStatisticsBinding
import timber.log.Timber

class StatisticsFragment : Fragment() {
    private lateinit var adapter: DeckAdapterStatistics

    val viewModel: StatisticsViewModel by lazy {
        ViewModelProvider(this).get(StatisticsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentStatisticsBinding>(
            inflater,
            R.layout.fragment_statistics,
            container,
            false
        )

        adapter = DeckAdapterStatistics()
        //binding.deckListRecyclerView.adpater = adapter

        viewModel.decks.observe(viewLifecycleOwner) {
            var info = String()
            for (deck in it)
                info += "The deck named ${deck.deck.name} has ${deck.cards.size} cards\n"
            Snackbar.make(requireView(), info, Snackbar.LENGTH_SHORT).show()
            var nDecks = it.size
            //binding.nDecks = nDecks
        }

        viewModel.loadDeckId("")

        viewModel.deckWithCards.observe(viewLifecycleOwner) {
            val deck = it[0].deck
            val cards = it[0].cards
            Timber.i("El mazo " + deck.name + " tiene " + cards.size + " tarjetas")
        }


//Tirar erro dps
        /*Timber.i("The hard value is " + hard!!.toFloat())

        val pieEntry: MutableList<PieEntry> = mutableListOf()
        pieEntry.add(PieEntry(hard!!.toFloat(), "Difficult"))
        pieEntry.add(PieEntry(doubt!!.toFloat(), "Doubt"))
        pieEntry.add(PieEntry(easy!!.toFloat(), "Easy"))
        binding.piechart.invalidate()

        val pieDataSet = PieDataSet(pieEntry, "Kotlin App")
        pieDataSet.valueTextSize = 15f
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val pieData = PieData(pieDataSet)

        binding.piechart.animateY(1500, Easing.EaseInOutQuad)
        binding.piechart.legend.orientation = Legend.LegendOrientation.VERTICAL
        binding.piechart.setUsePercentValues(true)
        binding.piechart.data = pieData
        binding.piechart.description.text = "Cards App"
        binding.piechart.centerText = "CARDS APP"
        binding.piechart.invalidate()
        return binding.root*/

//Para experimento
        val pieEntry: MutableList<PieEntry> = mutableListOf()
        pieEntry.add(PieEntry(30f, "Difficult"))
        pieEntry.add(PieEntry(5f, "Doubt"))
        pieEntry.add(PieEntry(10f, "Easy"))

        val pieDataSet = PieDataSet(pieEntry, "Kotlin App")
        pieDataSet.valueTextSize = 15f
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val pieData = PieData(pieDataSet)

        binding.piechart.animateY(1500, Easing.EaseInOutQuad)
        binding.piechart.legend.orientation = Legend.LegendOrientation.VERTICAL
        binding.piechart.setUsePercentValues(true)
        binding.piechart.data = pieData
        binding.piechart.description.text = "Cards App"
        binding.piechart.centerText = "CARDS APP"

        return binding.root
    }
}