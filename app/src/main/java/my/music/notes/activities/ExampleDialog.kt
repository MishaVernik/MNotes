package my.music.notes.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import my.music.notes.R
import java.io.IOException
import java.io.OutputStreamWriter

class ExampleDialog : AppCompatDialogFragment() {
    private var editTextMusicName: EditText? = null
    private val editTextPassword: EditText? = null
    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.layout_dialog, null)
        builder.setView(view)
                .setTitle("Music name")
                .setNegativeButton("cancel") { dialogInterface, i -> }
                .setPositiveButton("ok") { dialogInterface, i ->
                    val musicName = editTextMusicName!!.text.toString()
                    listener!!.applyTexts(musicName)
                }
        editTextMusicName = view.findViewById(R.id.music_name)
        return builder.create()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as ExampleDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() +
                    "must implement ExampleDialogListener")
        }
    }

    interface ExampleDialogListener {
        fun applyTexts(musicName: String?)
    }
}