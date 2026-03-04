package com.example.unscramble.ui

data class GameUIState(
    val currentScrambleWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGameOver: Boolean = false
)