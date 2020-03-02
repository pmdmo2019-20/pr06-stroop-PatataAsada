package es.iessaladillo.pedrojoya.stroop.base

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import es.iessaladillo.pedrojoya.stroop.R

object DialogGenerator {
    fun showDialog(title:String,message:String,context: Context) {
        val dialog: Dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.help_accept) { _, _-> }
            .create()
        dialog.show()
    }
}