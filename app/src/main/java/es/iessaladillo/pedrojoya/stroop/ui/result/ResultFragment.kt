package es.iessaladillo.pedrojoya.stroop.ui.result

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.android.synthetic.main.user_selected.*


class ResultFragment : Fragment() {

    val navCtrl:NavController by lazy {
        findNavController()
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
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
        setupView()
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(result_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.game_result_title)
        setupBackNavigation(viewmodel.fromGameFragment)
    }

    private fun setupBackNavigation(fromGameFragment: Boolean) {
        if(fromGameFragment) {
            viewmodel.fromGameFragment = false
            result_toolbar.setNavigationOnClickListener {
                navCtrl.popBackStack(R.id.mainFragment,false)
            }
        }
        else result_toolbar.setNavigationOnClickListener{
            navCtrl.navigateUp()
        }
    }

    private fun setupView() {
        txtCorrectAnswers.text = viewmodel.gameToShow!!.game.correct.toString()
        txtIncorrectAnswers.text = (viewmodel.gameToShow!!.game.words - viewmodel.gameToShow!!.game.correct).toString()
        txtPoints.text = viewmodel.gameToShow!!.game.points.toString()
        img_player.setImageResource(viewmodel.gameToShow!!.player.imgResId)
        lbl_player_name.text = viewmodel.gameToShow!!.player.name
    }

}
