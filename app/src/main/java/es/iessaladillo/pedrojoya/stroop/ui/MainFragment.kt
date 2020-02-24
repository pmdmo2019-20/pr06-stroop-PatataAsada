package es.iessaladillo.pedrojoya.stroop.ui

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.R
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private val navCtrl: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println(item.itemId)
        return when(item.itemId){
            R.id.mnuHelp -> {
                showDialog(getString(R.string.dashboard_help_description))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showDialog(message: String) {
        val dialog: Dialog = AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.help_title))
            .setMessage(message)
            .setPositiveButton(R.string.help_accept) { _, _-> }
            .create()
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
        setupToolbar()
        setupObservers()
        setupButtons()
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
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
