package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
    private val _uiState =
        MutableStateFlow(GameUIState()) //Observable dodne los cambios se actualizan automáticamente
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow();

    private fun elegirPalabraDesordenada(): String {
        var currentWords: String
        currentWords = allWords.random();
        if (usedWords.contains(currentWords)) {
            return elegirPalabraDesordenada()
        }else{
            usedWords.add(currentWords)
            currentWord = currentWords
            return shuffleCurrentWord(currentWords)
        }
    }

    private fun shuffleCurrentWord(word: String): String{
        val temWord = word.toCharArray()
        temWord.shuffle()
        while (String(temWord).equals(word)){
            temWord.shuffle()
        }
        return String(temWord)
    }

    fun resetGame(){
        usedWords.clear()
        _uiState.value = GameUIState(currentScrambleWord = elegirPalabraDesordenada())
    }

    init {
        resetGame()
    }

    fun checkUserGuess(){
        if (userGuess.equals(currentWord, ignoreCase = true)){

        }else{
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

}