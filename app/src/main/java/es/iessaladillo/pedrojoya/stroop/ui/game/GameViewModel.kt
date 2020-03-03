package es.iessaladillo.pedrojoya.stroop.ui.game

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.stroop.R
import kotlin.concurrent.thread
import kotlin.random.Random


class GameViewModel(var time: Int, val wordTime: Int, var gamemode: String, var attempts: Int) :
    ViewModel() {

    @Volatile
    private var isGameFinished: Boolean = false
    @Volatile
    private var currentWordMillis: Int = 0
    @Volatile
    private var millisUntilFinished: Int = 0
    private val handler: Handler = Handler()

    private var _correctGuess: MutableLiveData<Int> = MutableLiveData(0)
    val correctGuess: LiveData<Int> get() = _correctGuess

    private var _words: MutableLiveData<Int> = MutableLiveData(0)
    val words: LiveData<Int> get() = _words

    private var _gameState: MutableLiveData<Boolean> = MutableLiveData(isGameFinished)
    val gameState: LiveData<Boolean> get() = _gameState

    private val colorsArray: Array<String> = arrayOf("Red", "Blue", "Green", "Yellow")

    private var _wordColor: MutableLiveData<String> = MutableLiveData("")
    private var _WordString: MutableLiveData<String> = MutableLiveData("")
    val wordString: LiveData<String> get() = _WordString
    val wordColor: LiveData<String> get() = _wordColor

    private val wordTruth: Boolean get() = _wordColor.value!!.equals(_WordString.value!!)

    private var _attemnptsLeft:MutableLiveData<Int> = MutableLiveData(attempts)
    val attemptsLeft:LiveData<Int> get() = _attemnptsLeft

    private fun onGameTimeTick(millisUntilFinished: Int) {
        // TODO
    }

    private fun onGameTimeFinish() {
        isGameFinished = true
        _gameState.value = isGameFinished
    }

    fun nextWord() {
        _wordColor.value = colorsArray[Random.nextInt(colorsArray.size - 1)]
        _WordString.value = colorsArray[Random.nextInt(colorsArray.size - 1)]
    }

    fun checkRight() {
        currentWordMillis = 0
        checkTruth(true)
        nextWord()
    }

    fun checkWrong() {
        currentWordMillis = 0
        checkTruth(false)
        nextWord()
    }

    private fun checkTruth(guess: Boolean) {
        if (wordTruth == guess) _correctGuess.value = _correctGuess.value!! + 1
        _words.value = _words.value!! + 1
    }

    fun startGameThread(gameTime: Int, wordTime: Int) {
        millisUntilFinished = gameTime
        currentWordMillis = 0
        isGameFinished = false
        val checkTimeMillis: Int = wordTime / 2
        nextWord()
        thread {
            try {
                while (!isGameFinished) {
                    Thread.sleep(checkTimeMillis.toLong())
                    // Check and publish on main thread.
                    handler.post {
                        if (!isGameFinished) {
                            if (currentWordMillis >= wordTime) {
                                currentWordMillis = 0
                                nextWord()
                            }
                            currentWordMillis += checkTimeMillis
                            millisUntilFinished -= checkTimeMillis
                            onGameTimeTick(millisUntilFinished)
                            if (millisUntilFinished <= 0) {
                                onGameTimeFinish()
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isGameFinished = true
    }

}