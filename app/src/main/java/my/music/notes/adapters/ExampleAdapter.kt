package my.music.notes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.example_item.view.*
import my.music.notes.R
import my.music.notes.models.ExampleItem

// ListAdapter out of the box update list async on each submitList + nice animation
class ExampleAdapter(
        private val onItemClicked: (ExampleItem, Int) -> Unit,
        private val onItemDeleteClicked: (ExampleItem, Int) -> Unit
) : ListAdapter<ExampleItem, ExampleAdapter.ExampleViewHolder>(diffUtilItemCallback) {

    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<ExampleItem>() {
            override fun areItemsTheSame(oldItem: ExampleItem, newItem: ExampleItem): Boolean {
                return oldItem.uuid == newItem.uuid //todo add some id's?
            }

            override fun areContentsTheSame(oldItem: ExampleItem, newItem: ExampleItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ExampleViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind(model: ExampleItem) {
            itemView.imageView.setImageResource(model.imageResource)
            itemView.textView1.text = model.firstText
            itemView.textView2.text = model.secondaryText
            itemView.image_item_delete.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemDeleteClicked(model, adapterPosition)
            }
            itemView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemClicked(model, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        return ExampleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}