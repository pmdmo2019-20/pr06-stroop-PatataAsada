package es.iessaladillo.pedrojoya.stroop.ui.game

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.POINTS_PER_CORRECT_ANSWER

import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.model.Game
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import kotlinx.android.synthetic.main.fragment_game.*
import java.lang.IllegalStateException


class GameFragment : Fragment() {

    val navCtrl: NavController by lazy {
        findNavController()
    }

    val gameViewmodel: GameViewModel by viewModels {
        GameViewModelFactory(
            viewmodel.settings.getString(getString(R.string.prefGameTime_key), "20000")!!.toInt(),
            viewmodel.settings.getString(getString(R.string.prefWordTime_key), "1000")!!.toInt(),
            viewmodel.settings.getString(getString(R.string.prefGameMode_key), "Time")!!,
            viewmodel.settings.getString(getString(R.string.prefAttempts_key), "5")!!.toInt()
        )
    }

    lateinit var viewmodel: MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
        setupObservers()
        setupButtons()
        setupProgressBar()
        setupGame()
    }

    private fun setupProgressBar() {
        var i = 0
        pbTime.max = gameViewmodel.time
        val countDownTimer = object : CountDownTimer(gameViewmodel.time.toLong(), 1000) {
            override fun onFinish() = try {
                pbTime.progress = gameViewmodel.time / 1000
            }catch (error: IllegalStateException) {}

            override fun onTick(p0: Long) = try {
                i++
                pbTime.progress = i * 1000
            } catch (error: IllegalStateException) {}

        }.also {
            it.start()
        }

    }

    private fun setupGame() {
        gameViewmodel.startGameThread(gameViewmodel.time, gameViewmodel.wordTime)
    }

    private fun setupObservers() {
        gameViewmodel.gameState.observe(this) {
            if (gameViewmodel.gameState.value!!) finishGame()
        }
        gameViewmodel.wordColor.observe(this) { updateColor() }
        gameViewmodel.wordString.observe(this) { lblWord.text = gameViewmodel.wordString.value}
        gameViewmodel.words.observe(this) { txtWords.text = gameViewmodel.words.value.toString()}
        gameViewmodel.correctGuess.observe(this) {
            txtCorrect.text = gameViewmodel.correctGuess.value.toString()
            if(gameViewmodel.gamemode!="Attempts") txtExtra.text = (gameViewmodel.correctGuess.value!! * POINTS_PER_CORRECT_ANSWER).toString()
        }
        if(gameViewmodel.gamemode=="Attempts") gameViewmodel.attemptsLeft.observe(this) { txtExtra.text = gameViewmodel. attemptsLeft.value.toString()}
    }

    private fun updateColor() {
        when (gameViewmodel.wordColor.value!!) {
            "Red" -> lblWord.setTextColor(resources.getColor(R.color.gameRed))
            "Blue" -> lblWord.setTextColor(resources.getColor(R.color.gameBlue))
            "Green" -> lblWord.setTextColor(resources.getColor(R.color.gameGreen))
            "Yellow" -> lblWord.setTextColor(resources.getColor(R.color.gameYellow))
            else -> lblWord.setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun finishGame() {
        viewmodel.createGame(
            Game(
                0,
                txtWords.text.toString().toInt(),
                txtCorrect.text.toString().toInt(),
                gameViewmodel.gamemode,
                gameViewmodel.time,
                viewmodel.selectedPlayer.value!!.id
            )
        )
        viewmodel.fromGameFragment = true
        navCtrl.navigate(R.id.action_gameFragment_to_resultFragment)
    }

    private fun setupButtons() {
        btnRight.setOnClickListener { gameViewmodel.checkRight() }
        btnWrong.setOnClickListener { gameViewmodel.checkWrong() }
    }
}
