package projeto.cards

import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*
import kotlin.math.max
import kotlin.math.roundToLong

@Entity(tableName = "cards_table")
open class Card(
    @ColumnInfo(name = "card_question")
    var question: String,
    var answer: String,
    var date: String = LocalDateTime.now().toString(),
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var repetitions: Int = 0,
    var interval: Long = 1L,
    var nextPracticeDate: String = date,
    var easiness: Double = 2.5,
    var author: String? = null,
    var quality: Int = -1,
    var answered: Boolean = false,
    var no_cards: Boolean = false,
    var deckId: String = UUID.randomUUID().toString()
) {
    constructor() : this(
        "question",
        "answer",
        LocalDateTime.now().toString(),
        UUID.randomUUID().toString(),
        0
    )

    open fun show() {
        print(" $question (Enter to see answer)")
        readLine()
        print(" $answer (Type 0 -> Difficult 3 -> Doubt 5 -> Easy): ")
        quality = readLine()!!.toInt()
    }

    fun update_card(quality: Int) {
        this.quality = quality
        update(LocalDateTime.now())
    }

    fun update(currentDate: LocalDateTime) {
        easiness = max(1.3, easiness + 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))

        repetitions = if (quality < 3) 0
        else repetitions + 1

        interval = if (repetitions <= 1) 1
        else if (repetitions == 2) 6
        else (interval * easiness).roundToLong()

        nextPracticeDate = currentDate.plusDays(interval).toString()
        toString()
    }

    fun details() {
        println(
            " eas = ${"%.2f".format(easiness)} rep = $repetitions int = $interval next = ${
                nextPracticeDate.substring(
                    0..9
                )
            }\n"
        )
    }

    fun show_deck() {
        println(" $question -> $answer")
    }

    fun isDue(currentDate: LocalDateTime) {
        if (currentDate >= LocalDateTime.parse(nextPracticeDate)) {
            show()
            update(currentDate)
            details()
        }
    }

    fun isdue(date: LocalDateTime): Boolean {
        return date >= LocalDateTime.parse(nextPracticeDate)
    }

    fun simulate(period: Long) {
        println("Simulation of the card $question:")
        var now = LocalDateTime.now()
        var another: String = now.toString()

        for (i in 1..period) {
            println("Date: ${another.substring(0..9)}")
            isDue(now)
            now = now.plusDays(1)
            another = now.toString()
        }
    }
}