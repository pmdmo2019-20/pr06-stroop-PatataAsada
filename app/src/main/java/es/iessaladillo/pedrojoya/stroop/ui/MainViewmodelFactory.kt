package es.iessaladillo.pedrojoya.stroop.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.AppDatabase

class MainViewmodelFactory(private val application: Application, val database: AppDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewmodel::class.java)) {
            return MainViewmodel(application, database) as T
        }
        throw IllegalArgumentException("Must provide MainViewmodel class")
    }

}