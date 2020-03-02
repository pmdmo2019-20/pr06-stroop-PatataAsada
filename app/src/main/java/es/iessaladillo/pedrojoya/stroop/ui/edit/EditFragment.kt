package es.iessaladillo.pedrojoya.stroop.ui.edit

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.avatars
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodelFactory
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

    private fun setSelectedAvatar(avatar: Int) {
        viewmodel.changeAvatar(avatar)
    }

    private lateinit var viewmodel: MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewmodel.playerCreatorTrigger) {
            inflater.inflate(R.menu.menu, menu)
        } else {
            inflater.inflate(R.menu.menu_with_delete, menu)
            for (x in 0 until menu.size()) {
                tintMenuIcon(activity, menu.getItem(x))
            }
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuDelete -> {

                if (!viewmodel.playerCreatorTrigger) {
                    val dialog: Dialog = AlertDialog.Builder(context!!)
                        .setTitle(getString(R.string.player_deletion_title))
                        .setMessage(getString(R.string.player_deletion_message))
                        .setPositiveButton(getText(R.string.player_deletion_yes)) { _, _ ->
                            viewmodel.deletePlayer(viewmodel.selectedPlayer.value!!)
                            exitEditFragment()
                        }
                        .setNegativeButton(getText(R.string.player_deletion_no)) { _, _ -> }
                        .create()
                    dialog.show()
                }
                true
            }
            R.id.mnuHelp -> {
                if(viewmodel.playerCreatorTrigger){
                    DialogGenerator.showDialog(
                        getString(R.string.player_creation_title),
                        getString(R.string.player_creation_help_description),
                        context!!
                    )
                }else{
                    DialogGenerator.showDialog(
                        getString(R.string.player_edition_title),
                        getString(R.string.player_edition_help_description),
                        context!!
                    )
                }
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
        setupViewModel()
        setupPlayer()
        setupObservers()
        setupToolbar()
        setupReciclerView()
        setupButtons()
    }

    private fun setupViewModel() {
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
        if(!viewmodel.playerCreatorTrigger) viewmodel.changeAvatar(viewmodel.selectedPlayer.value!!.imgResId)
    }

    private fun setupPlayer() {
        if (!viewmodel.playerCreatorTrigger) {
            imgPlayer.setImageResource(viewmodel.selectedPlayer.value!!.imgResId)
            txtName.setText(viewmodel.selectedPlayer.value!!.name)
        }
    }

    private fun setupButtons() {
        fabSave.setOnClickListener { save() }
    }

    private fun save() {
        if (viewmodel.playerCreatorTrigger) {
            if (txtName.text.isNotEmpty() && viewmodel.newAvatar.value!! != 0)
                viewmodel.createPlayer(
                    Player(
                        0,
                        txtName.text.toString(),
                        viewmodel.newAvatar.value!!
                    )
                )
        } else {
            if (viewmodel.selectedPlayer.value!!.name.isNotEmpty()) viewmodel.updatePlayer(
                Player(
                    viewmodel.selectedPlayer.value!!.id,
                    txtName.text.toString(),
                    viewmodel.newAvatar.value!!
                )
            )
        }
        exitEditFragment()
    }

    private fun exitEditFragment() {
        navCtrl.popBackStack()
    }

    private fun setupReciclerView() {
        lstAvatars.run {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    RecyclerView.VERTICAL
                )
            )
            adapter = editFragmentAdapter
        }
        editFragmentAdapter.submitList(avatars)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(edit_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(if (viewmodel.playerCreatorTrigger) R.string.player_creation_title else R.string.player_edition_title)
        edit_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }

    }

    private fun setupObservers() {
        viewmodel.newAvatar.observe(this) {
            if (it != 0) imgPlayer.setImageResource(it)
        }
    }

    private fun tintMenuIcon(activity: FragmentActivity?, item: MenuItem?) {
        val normalDrawable: Drawable = item!!.icon
        val wrapDrawable = DrawableCompat.wrap(normalDrawable)
        DrawableCompat.setTint(wrapDrawable, context!!.resources.getColor(R.color.white))

        item.icon = wrapDrawable
    }
}
