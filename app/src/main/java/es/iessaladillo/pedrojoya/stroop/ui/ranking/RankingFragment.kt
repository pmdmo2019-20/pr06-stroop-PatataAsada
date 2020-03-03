package es.iessaladillo.pedrojoya.stroop.ui.ranking

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.DialogGenerator
import es.iessaladillo.pedrojoya.stroop.data.model.GameWithPlayer
import es.iessaladillo.pedrojoya.stroop.ui.MainViewmodel
import kotlinx.android.synthetic.main.fragment_ranking.*


class RankingFragment : Fragment() {
    private val navCtrl: NavController by lazy {
        findNavController()
    }

    lateinit var viewmodel: MainViewmodel

    private val rankingFragmentAdapter = RankingFragmentAdapter().also {
        it.setOnClickListener { position -> showResult(it.currentList[position]) }
    }

    private fun showResult(game: GameWithPlayer) {
        viewmodel.gameToShow = game
        navCtrl.navigate(R.id.action_rankingFragment_to_resultFragment)
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
                    getString(R.string.ranking_title),
                    getString(R.string.ranking_help_description),
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
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupReciclerView()
        setupObservers()
        setupToolbar()
        setupSpinner()
    }

    private fun setupSpinner() {
        spnGamemode.setSelection(chooseSelection())
        spnGamemode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewmodel.queryBy(null)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewmodel.queryBy(spnGamemode.selectedItem.toString())
            }
        }
    }

    private fun chooseSelection(): Int {
        return when(viewmodel.settings.getString(getString(R.string.prefRankingFilter_key),"All")){
            "Time" -> 1
            "Attempts" -> 2
            else -> 0
        }
    }

    private fun setupObservers() {
        viewmodel.listRanking.observe(this) {updateListRankings(viewmodel.listRanking.value!!)}
    }

    private fun updateListRankings(list: List<GameWithPlayer>) {
        lstRanking.post {
            rankingFragmentAdapter.submitList(list)
            lblEmptyView.visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            imgRanking.visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setupReciclerView() {
        lstRanking.run {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                    RecyclerView.VERTICAL)
            )
            adapter = rankingFragmentAdapter }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(ranking_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(if (viewmodel.playerCreatorTrigger) R.string.player_creation_title else R.string.player_edition_title)
        ranking_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }
    }

    private fun setupViewModel() {
        viewmodel = requireActivity().run {
            ViewModelProvider(this).get(MainViewmodel::class.java)
        }
    }
}
