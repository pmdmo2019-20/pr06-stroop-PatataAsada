package es.iessaladillo.pedrojoya.stroop.ui.assistant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.iessaladillo.pedrojoya.stroop.R
import kotlinx.android.synthetic.main.assistant_fragment.*
import kotlinx.android.synthetic.main.assistant_fragment_item7.*
import kotlinx.android.synthetic.main.settings_fragment.assistant_toolbar


class AssistantFragment : Fragment() {
    val navCtrl: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.assistant_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupReciclerView()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupReciclerView() {
        val listAdapter = AssistantFragmentAdapter(this)
        viewPager.adapter = listAdapter
        setupTabLayoutMediator()
        for (x in 0 until listAdapter.itemCount) {
            tabLayout.newTab().setIcon(R.drawable.tab_as_circle_indicator)
        }
    }

    private fun setupTabLayoutMediator() {
        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            tab.setIcon(R.drawable.tab_as_circle_indicator)
        }.attach()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                // Give time to animation
                tabLayout.postDelayed(Runnable { if(tab.position==7) btnFinish.setOnClickListener { navCtrl.navigateUp() } },200)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(assistant_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.assistant_title)
        assistant_toolbar.setNavigationOnClickListener { navCtrl.navigateUp() }
    }


}
