package my.music.notes.models

import androidx.annotation.DrawableRes
import java.util.*

data class ExampleItem(
        val uuid: String = UUID.randomUUID().toString(),
        @DrawableRes val imageResource: Int,
        val firstText: String,
        val secondaryText: String
)