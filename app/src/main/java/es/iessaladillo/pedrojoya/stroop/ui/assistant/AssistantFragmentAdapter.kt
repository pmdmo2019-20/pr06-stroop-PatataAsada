package es.iessaladillo.pedrojoya.stroop.ui.assistant

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.iessaladillo.pedrojoya.stroop.R

class AssistantFragmentAdapter(parent: Fragment) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int {
        return 8
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Fragment(R.layout.assistant_fragment_item0)
            1 -> Fragment(R.layout.assistant_fragment_item1)
            2 -> Fragment(R.layout.assistant_fragment_item2)
            3 -> Fragment(R.layout.assistant_fragment_item3)
            4 -> Fragment(R.layout.assistant_fragment_item4)
            5 -> Fragment(R.layout.assistant_fragment_item5)
            6 -> Fragment(R.layout.assistant_fragment_item6)
            else -> Fragment(R.layout.assistant_fragment_item7)
        }
    }


}