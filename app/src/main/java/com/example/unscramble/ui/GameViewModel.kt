package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
// Contiene la logica del juego
class GameViewModel: ViewModel() {
    // Guarda la palabra actual correcta
    private lateinit var currentWord: String
    // Guarda las palabras usadas
    private var usedWords: MutableSet<String> = mutableSetOf()
    // Guarda lo que escribe el jugador
    var userGuess by mutableStateOf("")
    // Guarda el estado del juego
    private val _uiState =
        MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow();

    // Elige una palabra al azar
    private fun elegirPalabraDesordenada(): String {
        var nuevaPalabra: String
        // Elige una palabra al azar
        nuevaPalabra = allWords.random()
        // Si ya fue mostrada la palabra, vuelve a elegir
        // Si no, la agrega a la lista
        if (usedWords.contains(nuevaPalabra)) {
            return elegirPalabraDesordenada()
        } else {
            usedWords.add(nuevaPalabra)
            // Actualiza la palabra actual
            currentWord = nuevaPalabra
            // Desordena la palabra
            return shuffleCurrentWord(nuevaPalabra)
        }
    }

    // Desordena la palabra
    private fun shuffleCurrentWord(word: String): String{
        // Convierte la palabra a un array de caracteres
        val temWord = word.toCharArray()
        // Desordena el array de caracteres
        temWord.shuffle()
        // Si la palabra desordenada es la misma que la original vuelve a desordenarla
        while (String(temWord).equals(word)){
            // Desordena el array de caracteres si la palabra es la misma
            temWord.shuffle()
        }
        return String(temWord)
    }

    fun resetGame(){
        // Borra las palabras usadas
        usedWords.clear()
        // Actualiza el estado del juego
        _uiState.value = GameUIState(currentScrambleWord = elegirPalabraDesordenada())
    }

    init {
        resetGame()
    }

    // Comprueba la palabra
    fun checkUserGuess(){
        // Comprueba si la palabra escrita es la misma que la actual
        if (userGuess.equals(currentWord, ignoreCase = true)){
            // Suma los puntos y actualiza el estado del juego
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }else{
            // Marca error y limpia la palabra
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    // Actualiza el estado del juego
    private fun updateGameState(updatedScore: Int){
        // Si ya se jugaron todas las palabras
        // Actualiza el estado del juego
        if (usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }else{
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambleWord = elegirPalabraDesordenada(),
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }
    }
    // Salta la palabra
    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }
}