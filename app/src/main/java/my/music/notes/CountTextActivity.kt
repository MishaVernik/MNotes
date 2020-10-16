package my.music.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class CountTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if the intent that started this activity has the ACTION_PROCESS_TEXT intent action
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {

            // Get selected text from intent extra using the EXTRA_PROCESS_TEXT key
            // And returning an empty string if it's null.
            val selectedText = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: ""
            Toast.makeText(this, "Selected Text has ${countWords(selectedText)} words", Toast.LENGTH_LONG).show()
            writeToFile(selectedText + "\n", this)
            // Very important to end activity immediately if you're not display a layout
            finish()
        }
    }
    private fun writeToFile(data: String, context: Context) {
        try {

            val outputStreamWriter = OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_APPEND))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }
    private fun countWords(textBlock: String): Int {
        val words = textBlock.length;
        return words
    }
}