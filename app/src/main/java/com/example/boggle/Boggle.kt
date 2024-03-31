package com.example.boggle

import android.os.Bundle
import androidx.fragment.app.Fragment

class Boggle : Fragment() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        button = Button (
            value = "",
            row = 0,
            column = 0,
            isSelected = false,
        )
    }
}