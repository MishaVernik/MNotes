package my.music.notes.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import my.music.notes.R
import my.music.notes.adapters.ExampleAdapter
import my.music.notes.models.ExampleItem
import my.music.notes.utils.clearFile
import my.music.notes.utils.readFromFile
import my.music.notes.utils.writeIntoFile
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CONFIG_FILE_NAME = "config.txt"
    }

    //todo remove
    private var touchCount: Int = 0

    //todo this should be in ViewModel and be observed via LiveData or StateFlow
    private val items: MutableList<ExampleItem> = mutableListOf()

    private val adapter by lazy {
        ExampleAdapter(
                onItemClicked = this::onItemTouched,
                onItemDeleteClicked = { _, position -> deleteItem(position) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter

        readFromFile()

        setupClicks()
    }

    private fun setupClicks() {
        button_insert?.setOnClickListener {
            insertItem(edittext_insert?.text?.toString()?.toIntOrNull() ?: 0)
        }

        button_remove?.setOnClickListener {
            deleteItem(edittext_remove?.text?.toString()?.toIntOrNull() ?: 0)
        }
    }

    private fun onItemTouched(model: ExampleItem, position: Int) {
        items[position] = model.copy(firstText = "${model.firstText} $touchCount")
        touchCount++
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer
    }

    private fun dumpAllSongs() {
        clearFile(this, CONFIG_FILE_NAME)
        items.forEach { model -> writeIntoFile(model.firstText + "\n") }
    }

    private fun insertItem(position: Int) {
        val realPosition = if (position > items.size) items.size else position
        val model = ExampleItem(imageResource = R.drawable.ic_android, firstText = "Song name?", secondaryText = "Singer?")
        items.add(realPosition, model)
        writeIntoFile(model.firstText + "\n")
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer
    }

    private fun deleteItem(position: Int) {
        if (items.isEmpty()) return
        val realPosition = if (position >= items.size) items.size - 1 else position
        items.removeAt(realPosition)
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer
        dumpAllSongs()
    }

    private fun writeIntoFile(data: String) {
        writeIntoFile(this, CONFIG_FILE_NAME) { outputStreamWriter ->
            outputStreamWriter.write(data)
        }
    }

    private fun readFromFile() {
        readFromFile(this, CONFIG_FILE_NAME) { inputStream ->
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var receiveString: String?
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                receiveString ?: continue
                items.add(ExampleItem(imageResource = R.drawable.ic_music, firstText = receiveString!!, secondaryText = "Singer"))
            }
        }
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer
    }

    override fun onDestroy() {
        super.onDestroy()
        dumpAllSongs()
    }

}