package com.example.boggle

data class Button(
    var value : String,
    var row : Int,
    var column : Int,
    var isSelected: Boolean,
)