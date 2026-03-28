package com.jakarispann.flashcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // SharedPreferences key constants
    private val PREFS_NAME = "FlashcardPrefs"
    private val KEY_USER_NAME = "user_name"

    private lateinit var createButton: Button
    private lateinit var viewCardsButton: Button
    private lateinit var saveNameButton: Button
    private lateinit var mapButton: Button
    private lateinit var sensorButton: Button
    private lateinit var etUserName: EditText
    private lateinit var tvWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createButton = findViewById(R.id.btnCreate)
        viewCardsButton = findViewById(R.id.btnViewCards)
        saveNameButton = findViewById(R.id.btnSaveName)
        mapButton = findViewById(R.id.btnViewMap)
        sensorButton = findViewById(R.id.btnSensor)
        etUserName = findViewById(R.id.etUserName)
        tvWelcome = findViewById(R.id.tvWelcome)

        // SharedPreferences: load saved name on launch so it persists after app close
        loadUserName()

        // SharedPreferences: save user's name when button is pressed
        saveNameButton.setOnClickListener {
            val name = etUserName.text.toString().trim()
            if (name.isNotEmpty()) {
                saveUserName(name)
                tvWelcome.text = "Welcome, $name!"
            }
        }

        // Navigate to Create Card screen
        createButton.setOnClickListener {
            val intent = Intent(this, CreateCardActivity::class.java)
            startActivity(intent)
        }

        // Navigate to View Cards screen
        viewCardsButton.setOnClickListener {
            val intent = Intent(this, ViewCardsActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Maps screen
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Sensor demo screen
        sensorButton.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }
    }

    // SharedPreferences: save the user's name to persistent storage
    private fun saveUserName(name: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    // SharedPreferences: read the user's name back from storage on app open
    private fun loadUserName() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedName = prefs.getString(KEY_USER_NAME, "")
        if (!savedName.isNullOrEmpty()) {
            etUserName.setText(savedName)
            tvWelcome.text = "Welcome back, $savedName!"
        }
    }
}
