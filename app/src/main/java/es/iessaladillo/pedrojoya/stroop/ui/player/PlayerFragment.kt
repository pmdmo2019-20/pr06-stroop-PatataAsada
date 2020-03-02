package es.iessaladillo.pedrojoya.stroop.ui.player

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodelFactory
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.user_selected.*


class PlayerFragment : Fragment() {

    private val navCtrl:NavController by lazy {
        findNavController()
    }

    private val playerAdapter = PlayerFragmentAdapter().also {
        it.setOnClickListener{ position ->
            setSelectedPlayer(it.currentList[position])
        }
    }
    private lateinit var viewmodel:MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnuHelp -> {
                DialogGenerator.showDialog(getString(R.string.help_title),getString(R.string.player_selection_help_description),context!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupObservers()
        setupToolbar()
        setupReciclerView()
        setupButtons()
    }

    private fun setupViewModel() {
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
    }

    private fun setupToolbar() {
            (activity as AppCompatActivity).setSupportActionBar(player_toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.player_selection_title)
            player_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }
    }

    private fun setupButtons() {
        btnEdit.setOnClickListener { edit(false) }
        fabCreate.setOnClickListener{ edit(true) }
    }

    private fun edit(bool:Boolean) {
        viewmodel.setCreationTrigger(bool)
        navCtrl.navigate(R.id.action_playerFragment_to_editFragment)
    }

    private fun setupReciclerView() {
        listPLayers.run {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                RecyclerView.VERTICAL)
            )
            adapter = playerAdapter }
    }

    private fun setSelectedPlayer(player: Player) {
        viewmodel.setSelectedPlayer(player)
    }

    private fun setupObservers() {
        viewmodel.selectedPlayer.observe(this) { updatePlayerSelected() }
        viewmodel.listPlayers.observe(this) {updateListPlayers(viewmodel.listPlayers.value!!)}
    }

    private fun updateListPlayers(list: List<Player>) {
        listPLayers.post {
            playerAdapter.submitList(list)
            lblEmptyView.visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun updatePlayerSelected() {
        if(viewmodel.selectedPlayer.value != null){
            img_player.setImageResource(viewmodel.selectedPlayer.value!!.imgResId)
            lbl_player_name.text = viewmodel.selectedPlayer.value!!.name
            btnEdit.visibility = View.VISIBLE
        }
    }


}
