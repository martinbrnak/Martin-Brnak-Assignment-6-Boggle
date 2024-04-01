package com.example.boggle

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GameFragment())
            //.replace(R.id.fragment_container_status, GameStatusFragment())
            .commit()

        val btnReset : android.widget.Button = findViewById(R.id.btnNewGame)
        btnReset.setOnClickListener {
            val gameFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? GameFragment
            gameFragment?.resetGame()
        }

    }





    }
