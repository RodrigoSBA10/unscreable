package com.example.unscramble.ui

data class GameUIState(
    val currentScrambleWord: String = "",
    val isGuessedWordWrong: Boolean = false,
)