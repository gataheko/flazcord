package com.jakarispann.flashcard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Database persistence: stores flashcard question/answer pairs in a local SQLite database
class FlashcardDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "flashcards.db"
        const val DATABASE_VERSION = 1

        // Table and column names
        const val TABLE_FLASHCARDS = "flashcards"
        const val COLUMN_ID = "id"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the flashcards table when the database is first created
        val createTable = """
            CREATE TABLE $TABLE_FLASHCARDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION TEXT NOT NULL,
                $COLUMN_ANSWER TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop and recreate on upgrade
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FLASHCARDS")
        onCreate(db)
    }

    // Insert a new flashcard into the database
    fun insertCard(question: String, answer: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION, question)
            put(COLUMN_ANSWER, answer)
        }
        return db.insert(TABLE_FLASHCARDS, null, values)
    }

    // Retrieve all flashcards as a list of Pair<question, answer>
    fun getAllCards(): List<Pair<String, String>> {
        val cards = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FLASHCARDS,
            arrayOf(COLUMN_QUESTION, COLUMN_ANSWER),
            null, null, null, null, "$COLUMN_ID DESC"
        )
        while (cursor.moveToNext()) {
            val question = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION))
            val answer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER))
            cards.add(Pair(question, answer))
        }
        cursor.close()
        return cards
    }
}
