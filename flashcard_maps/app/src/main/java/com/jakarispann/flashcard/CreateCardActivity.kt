package com.jakarispann.flashcard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateCardActivity : AppCompatActivity() {

    private lateinit var etQuestion: EditText
    private lateinit var etAnswer: EditText
    private lateinit var btnSaveCard: Button

    // Database helper for SQLite persistence
    private lateinit var dbHelper: FlashcardDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)

        etQuestion = findViewById(R.id.etQuestion)
        etAnswer = findViewById(R.id.etAnswer)
        btnSaveCard = findViewById(R.id.btnSaveCard)

        // Database: initialize the SQLite helper
        dbHelper = FlashcardDbHelper(this)

        // Database: save a new flashcard when the button is pressed
        btnSaveCard.setOnClickListener {
            val question = etQuestion.text.toString().trim()
            val answer = etAnswer.text.toString().trim()

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please enter both a question and answer.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Insert the card into the SQLite database
            val rowId = dbHelper.insertCard(question, answer)
            if (rowId != -1L) {
                Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show()
                etQuestion.text.clear()
                etAnswer.text.clear()
            } else {
                Toast.makeText(this, "Error saving card. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
