package com.example.boggle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.fragment.app.Fragment
import kotlin.collections.shuffle

class GameFragment : Fragment() {
    private lateinit var gridView: GridView

    // Define your 4x4 board of letters here
    private val boardButtons = Array(4) { row ->
        Array(4) { col ->
            Button("", row, col, isSelected = false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearButton : android.widget.Button = view.findViewById(R.id.btnClear)
        val submitButton : android.widget.Button = view.findViewById(R.id.btnSubmit)

        // Set click listeners
        clearButton.setOnClickListener { (clearButton) }
        submitButton.setOnClickListener { (submitButton) }
    }



    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gamefragment, container, false)
        gridView = view.findViewById(R.id.gridView)
        setupGridView()
        return view
    }

    private fun setupGridView() {
        var boggleDiceLetters = listOf(
            "A", "A", "A", "A", "B", "B", "C", "C", "D", "D", "D", "D",
            "E", "E", "E", "E", "F", "F", "G", "G", "H", "H", "I", "I",
            "I", "I", "J", "K", "L", "L", "L", "L", "M", "M", "N", "N",
            "N", "N", "O", "O", "O", "O", "P", "P", "Q", "R", "R", "R",
            "R", "S", "S", "S", "S", "T", "T", "T", "T", "U", "U", "U",
            "U", "V", "V", "W", "W", "X", "Y", "Y", "Z"
        ).toMutableList()



        boggleDiceLetters.shuffle()


        val flattenedButtons = boardButtons.flatten()

        val first16Buttons = flattenedButtons.take(16)

        first16Buttons.forEachIndexed { index, button ->
            button.value = boggleDiceLetters[index]
        }

        val buttonValues = boardButtons.flatten().map { it.value }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.letter_button,
            buttonValues
        )

        gridView.adapter = adapter
        gridView.numColumns = 4
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Handle item click here
            val row = position / 4
            val col = position % 4
            val button = boardButtons[row][col]
            button.isSelected = !button.isSelected
            // Update UI or perform any other action based on button selection
            adapter.notifyDataSetChanged() // Refresh grid view
        }
    }
}
