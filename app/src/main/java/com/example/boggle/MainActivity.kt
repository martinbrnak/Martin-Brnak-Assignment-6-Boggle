package com.example.boggle

import GameStatusFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GameFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_2, GameStatusFragment())
            .commit()
    }
}