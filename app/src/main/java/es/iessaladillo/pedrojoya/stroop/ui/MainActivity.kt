package es.iessaladillo.pedrojoya.stroop.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase
import kotlinx.android.synthetic.main.settings_fragment.*


class MainActivity : AppCompatActivity() {

    val viewmodel:MainViewmodel by viewModels {
        MainViewmodelFactory(application, AppDatabase.getInstance(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
