package my.music.notes.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.TextView
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


class MainActivity : AppCompatActivity(), ExampleDialog.ExampleDialogListener {

    companion object {
        private const val CONFIG_FILE_NAME = "config.txt"
    }

    private var textViewMusicName: TextView? = null
    private var headeraAddbutton: Button? = null
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

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_title_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.button_add) {
            // do something here
            openDialog()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.menu.custom_title_bar);

        textViewMusicName = findViewById<TextView>(R.id.music_name)
        headeraAddbutton = findViewById<Button>(R.id.button_add)
        headeraAddbutton?.setOnClickListener { openDialog() }

        recyclerView.adapter = adapter

        readFromFile()

        setupClicks()
    }
    fun openDialog() {
        val exampleDialog = ExampleDialog()
        exampleDialog.show(supportFragmentManager, "Music name dialog")
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
        items[position] = model.copy(firstText = "${model.firstText}")
        touchCount++
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer

        openNewTabWindow("https://music.youtube.com/search?q=" + model.firstText
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "+"), this)
    }

    fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context.startActivity(intents)
    }

    private fun dumpAllSongs() {
        clearFile(this, CONFIG_FILE_NAME)
        items.forEach { model -> writeIntoFile(model.firstText + "\n") }
    }

    private fun insertItem(position: Int) {
        val realPosition = if (position > items.size) items.size else position
        val model = ExampleItem(imageResource = R.drawable.ic_android, firstText = "Song name?", secondaryText = "Singer?")
        println("___###______________________###___________________")
        println(position)
        println("________________###_______________________####_____")
        items.add(realPosition, model)
        adapter.submitList(items.toMutableList()) // submit copy of the list to trigger AsyncDiffer
        writeIntoFile(model.firstText + "\n")
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
        items.clear()
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

    override fun onResume() {
        super.onResume()
        readFromFile()
    }

    override fun onDestroy() {
        super.onDestroy()
        dumpAllSongs()
    }

    override fun applyTexts(musicName: String?) {
        if (musicName != null) {
            writeIntoFile(musicName + "\n")
        }
        readFromFile()
        textViewMusicName?.setText(musicName);
    }

}