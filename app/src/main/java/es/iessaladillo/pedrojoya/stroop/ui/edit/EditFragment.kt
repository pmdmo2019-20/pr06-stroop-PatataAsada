package es.iessaladillo.pedrojoya.stroop.ui.edit

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.data.AvatarDatabase
import es.iessaladillo.pedrojoya.stroop.data.model.Avatar
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodelFactory
import es.iessaladillo.pedrojoya.stroop.ui.player.PlayerFragmentAdapter
import kotlinx.android.synthetic.main.fragment_edit.*


class EditFragment : Fragment() {

    private val navCtrl: NavController by lazy {
        findNavController()
    }

    private val editFragmentAdapter = EditFragmentAdapter().also {
        it.setOnClickListener { position ->
            setSelectedAvatar(it.currentList[position])
        }
    }

    private fun setSelectedAvatar(avatar: Avatar) {
        viewmodel.changeAvatar(avatar)
    }

    private val viewmodel: MainViewmodel by viewModels {
        MainViewmodelFactory(
            activity!!.application,
            AppDatabase.getInstance(activity!!.application)
        )
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
        return when (item.itemId) {
            R.id.mnuHelp -> {
                DialogGenerator.showDialog(
                    getString(R.string.player_edition_title),
                    getString(R.string.player_edition_help_description),
                    context!!
                )
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
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
        setupToolbar()
        setupReciclerView()
        setupButtons()
    }

    private fun setupButtons() {
        fabSave.setOnClickListener { save() }
    }

    private fun save() {
        if(viewmodel.playerCreatorTrigger){
            if (viewmodel.selectedPlayer.value!!.name.isNotEmpty()&&viewmodel.newPlayer.value!!.imgResId!=0) viewmodel.createPlayer(
                Player(0,txtName.text.toString(),viewmodel.newPlayer.value!!.imgResId)
            )
        }else{
            if (viewmodel.selectedPlayer.value!!.name.isNotEmpty()) viewmodel.updatePlayer(viewmodel.selectedPlayer.value!!)
        }
        navCtrl.popBackStack()
    }

    private fun setupReciclerView() {
        lstAvatars.run {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                    RecyclerView.VERTICAL)
            )
            adapter = editFragmentAdapter }
        editFragmentAdapter.submitList(AvatarDatabase.avatars)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(edit_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(if (viewmodel.playerCreatorTrigger) R.string.player_creation_title else R.string.player_edition_title)
        edit_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }
    }

    private fun setupObservers() {
        viewmodel.newPlayer.observe(this){
            if(it.imgResId!=0) imgPlayer.setImageResource(it.imgResId)
            fabSave.isEnabled = txtName.text.isNotEmpty()&&it.imgResId!=0
        }
    }
}
