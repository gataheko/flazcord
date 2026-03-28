package com.jakarispann.flashcard

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewCardsActivity : AppCompatActivity() {

    private lateinit var rvCards: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var dbHelper: FlashcardDbHelper

    // Hardcoded fallback flashcards shown when no user cards exist (satisfies minimum 5 items)
    private val defaultCards = listOf(
        Pair("What is the capital of France?", "Paris"),
        Pair("What is 2 + 2?", "4"),
        Pair("What planet is closest to the Sun?", "Mercury"),
        Pair("What is the chemical symbol for water?", "H2O"),
        Pair("Who wrote Romeo and Juliet?", "William Shakespeare")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cards)

        rvCards = findViewById(R.id.rvCards)
        tvEmpty = findViewById(R.id.tvEmpty)

        // Set up RecyclerView with a LinearLayoutManager
        rvCards.layoutManager = LinearLayoutManager(this)

        // Database: read all saved flashcards from SQLite on activity launch
        dbHelper = FlashcardDbHelper(this)
        loadCards()
    }

    // Database: load and display all flashcards from the database
    private fun loadCards() {
        val dbCards = dbHelper.getAllCards()

        // Use database cards if available, otherwise show hardcoded defaults
        val cards = if (dbCards.isNotEmpty()) dbCards else defaultCards

        tvEmpty.visibility = android.view.View.GONE
        rvCards.visibility = android.view.View.VISIBLE

        // Create and set the custom adapter; show a Toast when an item is clicked
        val adapter = FlashcardAdapter(cards) { card ->
            Toast.makeText(this, "Q: ${card.first}", Toast.LENGTH_SHORT).show()
        }
        rvCards.adapter = adapter
    }
}
