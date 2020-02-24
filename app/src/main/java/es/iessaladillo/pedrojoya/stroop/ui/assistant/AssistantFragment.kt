package es.iessaladillo.pedrojoya.stroop.ui.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.iessaladillo.pedrojoya.stroop.R


class AssistantFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.assistant_fragment, container, false)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
