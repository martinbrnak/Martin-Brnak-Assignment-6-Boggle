package com.example.boggle

data class Button(
    var value : String,
    val row : Int,
    val column : Int,
    var isSelected: Boolean,
)