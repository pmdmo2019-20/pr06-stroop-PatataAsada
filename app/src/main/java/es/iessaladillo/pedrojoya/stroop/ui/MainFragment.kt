package es.iessaladillo.pedrojoya.stroop.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.lbl_player_name


class MainFragment : Fragment() {

    private val navCtrl: NavController by lazy {
        findNavController()
    }

    private lateinit var viewmodel:MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        for(x in 0 until menu.size()){
            tintMenuIcon(activity,menu.getItem(x))
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun tintMenuIcon(activity: FragmentActivity?, item: MenuItem?) {
        val normalDrawable: Drawable = item!!.icon
        val wrapDrawable = DrawableCompat.wrap(normalDrawable)
        DrawableCompat.setTint(wrapDrawable, context!!.resources.getColor(R.color.white))

        item.icon = wrapDrawable
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupToolbar()
        setupObservers()
        setupButtons()
    }
    private fun setupViewModel() {
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(main_fragment_toolbar)
    }

    private fun setupButtons() {
        cvPlay.setOnClickListener { navigate("Play") }
        cvPlayer.setOnClickListener { navigate("Player") }
        cvAssistant.setOnClickListener { navigate("Assistant") }
        cvSettings.setOnClickListener { navigate("Settings") }
        cvAbout.setOnClickListener { navigate("About") }
        cvRanking.setOnClickListener { navigate("Ranking") }
        actual_player.setOnClickListener{ navigate("Player") }
    }

    private fun navigate(to: String) {
        when (to) {
            "Play" -> navCtrl.navigate(R.id.action_mainFragment_to_gameFragment)
            "Player" -> navCtrl.navigate(R.id.action_mainFragment_to_playerFragment)
            "Assistant" -> navCtrl.navigate(R.id.action_mainFragment_to_assistantFragment)
            "Settings" -> navCtrl.navigate(R.id.action_mainFragment_to_settingsFragment)
            "About" -> navCtrl.navigate(R.id.action_mainFragment_to_aboutFragment)
            "Ranking" -> navCtrl.navigate(R.id.action_mainFragment_to_rankingFragment)
        }
    }

    private fun setupObservers() {
        viewmodel.selectedPlayer.observe(this){changePlayer()}
    }

    private fun changePlayer() {
        if(viewmodel.selectedPlayer.value != null) {
            lbl_player_name.text = viewmodel.selectedPlayer.value!!.name
            img_player.setImageResource(viewmodel.selectedPlayer.value!!.imgResId)
        }else{
            lbl_player_name.text = getString(R.string.player_selection_no_player_selected)
            img_player.setImageResource(R.drawable.logo)
        }
    }
}
