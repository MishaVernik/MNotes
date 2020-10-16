package my.music.notes.utils

import android.content.Context
import android.util.Log
import java.io.*

fun writeIntoFile(context: Context, fileName: String, writer: (OutputStreamWriter) -> Unit): Boolean {
    val file = File(context.filesDir, fileName)
    if (!file.exists()) {
        try {
            if (!file.createNewFile()) return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    if (!file.canWrite()) return false
    return try {
        val outputStreamWriter = OutputStreamWriter(FileOutputStream(file, true))
        writer(outputStreamWriter)
        outputStreamWriter.close()
        true
    } catch (e : IOException) {
        e.printStackTrace()
        false
    }
}

fun readFromFile(context: Context, fileName: String, reader: (InputStream) -> Unit): Boolean {
    val file = File(context.filesDir, fileName)
    if (!file.exists() || !file.canRead()) return false
    return try {
        val inputStream = BufferedInputStream(FileInputStream(file))
        reader(inputStream)
        inputStream.close()
        true
    } catch (e : IOException) {
        e.printStackTrace()
        false
    }
}

fun clearFile(context: Context, fileName: String): Boolean {
    return try {
        val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.write("")
        outputStreamWriter.close()
        true
    } catch (e: IOException) {
        Log.e("Exception", "File write failed: $e")
        false
    }
}

