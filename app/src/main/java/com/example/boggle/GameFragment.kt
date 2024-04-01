package com.example.boggle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.abs

class GameFragment : Fragment() {
    private lateinit var boardButtons: Array<Array<Button>>
    private lateinit var uniqueWords: Array<String>
    private lateinit var gridView: GridView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var newWord : TextView
    private var selectedButtons: MutableList<Button> = mutableListOf()
    private lateinit var clearBtn : android.widget.Button
    private lateinit var submitBtn : android.widget.Button
    private lateinit var englishWords: Set<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gamefragment, container, false)
        // Initialize the boardButtons array
        boardButtons = Array(4) { row ->
            Array(4) { col ->
                Button("", row, col, isSelected = false)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            englishWords = downloadWordList()
        }
        gridView = view.findViewById(R.id.gridView)
        newWord = view.findViewById(R.id.newWord)
        clearBtn = view.findViewById(R.id.btnClear)
        submitBtn = view.findViewById(R.id.btnSubmit)
        uniqueWords = arrayOf()
        setupGridView()


        clearBtn.setOnClickListener {
            selectedButtons.forEach { it.isSelected = false }
            selectedButtons = mutableListOf()
            newWord.text = ""

        }

        submitBtn.setOnClickListener {
            val word = newWord.text.toString()
            var word_score = 0
            var scoreTextView = activity?.findViewById<TextView>(R.id.score_num)
            var score = scoreTextView?.text.toString().toInt()
            var multiplier = false
            if (isValidWord(word, selectedButtons)) {
                word_score = 0

                for (button in selectedButtons) {
                    if ("AEIOU".contains(button.value)) {
                        word_score += 5
                    } else {
                        word_score++
                    }
                    if ("SZPXQ".contains(button.value)) {
                        multiplier = true
                    }
                }

                if (multiplier) {
                    word_score *= 2
                }

                if (uniqueWords.contains(word)) {
                    word_score = 0
                } else {
                    uniqueWords += word
                }
            } else {
                word_score -= 10
            }
            score += word_score
            scoreTextView?.text = score.toString()
            selectedButtons.forEach { it.isSelected = false }
            selectedButtons.clear()
            newWord.text = ""
        }



        return view

    }

    private suspend fun downloadWordList(): Set<String> = withContext(Dispatchers.IO) {
        val url = URL("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val responseCode = connection.responseCode

        val words = mutableSetOf<String>()

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            while (inputStream.readLine().also { inputLine = it } != null) {
                words.add(inputLine!!)
            }
            inputStream.close()
        }

        return@withContext words
    }

    fun isValidWord(word: String, selectedButtons: MutableList<Button>): Boolean = runBlocking {
        // Check if the word has at least 4 characters
        if (word.length < 4) {
            return@runBlocking false
        }

        // Initialize variables to track the counts of vowels and unique characters
        var vowelCount = 0
        var prev_col = selectedButtons[0].column
        var prev_row = selectedButtons[0].row
        var currentCol: Int
        var currentRow: Int
        for (button in selectedButtons) {
            currentCol = button.column
            currentRow = button.row
            if ("AEIOU".contains(button.value))
                vowelCount++
            if ((abs(prev_col - currentCol) > 1) or (abs(prev_row - currentRow) > 1)) {
                return@runBlocking false
            }
            prev_col = currentCol
            prev_row = currentRow
        }

        // Check if the word utilizes a minimum of two vowels
        if (vowelCount < 2) {
            return@runBlocking false
        }

        // Check if the word exists in the Merriam-Webster Dictionary
        return@runBlocking englishWords.contains(word.lowercase())
    }

    private fun setupGridView() {
        // Define your 4x4 board of letters here
        shuffleLetters()

        // Initialize the adapter with button values
        var buttonValues = boardButtons.flatten().map { it.value }
        adapter = ArrayAdapter(requireContext(), R.layout.letter_button, buttonValues)

        // Set the adapter to the GridView
        gridView.adapter = adapter
        gridView.numColumns = 4

        // Set item click listener for grid view
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val row = position / 4
            val col = position % 4
            val button = boardButtons[row][col]

            adapter.notifyDataSetChanged() // Refresh grid view

            // Add the selected button to the list
            if (!button.isSelected) {
                button.isSelected = !button.isSelected
                // Assuming `selectedButtons` is a list of selected buttons
                selectedButtons.add(button)

                // Update the newWord text with the value of the selected button
                newWord.text = newWord.text.toString() + button.value
            } else {
            }

        }
        }


    fun resetGame() {
        // Shuffle letters and update grid view
        shuffleLetters()
        updateGridView()
    }
    private fun shuffleLetters() {
        // Define your list of letters to shuffle
        val boggleDiceLetters = listOf(
            "A", "A", "A", "A", "B", "B", "C", "C", "D", "D", "D", "D",
            "E", "E", "E", "E", "F", "F", "G", "G", "H", "H", "I", "I",
            "I", "I", "J", "K", "L", "L", "L", "L", "M", "M", "N", "N",
            "N", "N", "O", "O", "O", "O", "P", "P", "Q", "R", "R", "R",
            "R", "S", "S", "S", "S", "T", "T", "T", "T", "U", "U", "U",
            "U", "V", "V", "W", "W", "X", "Y", "Y", "Z"
        ).toMutableList()

        // Shuffle the letters
        boggleDiceLetters.shuffle()

        // Assign the shuffled letters to the buttons in your game grid
        var index = 0
        for (row in boardButtons.indices) {
            for (col in boardButtons[row].indices) {
                boardButtons[row][col].value = boggleDiceLetters[index++]
            }
        }
    }

    private fun updateGridView() {
        // Update grid view with new data
        val buttonValues = boardButtons.flatten().map { it.value }
        adapter.clear()
        adapter.addAll(buttonValues)
        adapter.notifyDataSetChanged()
    }
}
