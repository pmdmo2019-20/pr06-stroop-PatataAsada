package es.iessaladillo.pedrojoya.stroop.ui.result

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.android.synthetic.main.fragment_result.*


class ResultFragment : Fragment() {

    val navCtrl:NavController by lazy {
        findNavController()
    }

    lateinit var viewmodel: MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.mnuHelp -> {
                DialogGenerator.showDialog(getString(R.string.help_title),getString(R.string.dashboard_help_description),context!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

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
        setupView()
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(ranking_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(if (viewmodel.playerCreatorTrigger) R.string.player_creation_title else R.string.player_edition_title)
        ranking_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }
    }

    private fun setupView() {
        txtCorrectAnswers.text = viewmodel.gameToShow!!.game.correct.toString()
        txtIncorrectAnswers.text = (viewmodel.gameToShow!!.game.words - viewmodel.gameToShow!!.game.correct).toString()
        txtPoints.text = viewmodel.gameToShow!!.game.points.toString()
    }

}
