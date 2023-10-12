package tia.sarwoedhi.storyapp.ui.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.databinding.ItemStoryBinding
import tia.sarwoedhi.storyapp.ui.detail_story.DetailStoryActivity
import tia.sarwoedhi.storyapp.utils.Constant.EXTRA_STORY

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryEntity) {
            with(binding) {
                Glide.with(itemView.context).load(story.photoUrl).into(binding.imageView)
                title.text = story.name
                description.text = story.description
                date.text = story.createdAt
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(EXTRA_STORY, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imageView, "image"),
                        Pair(binding.title, "name"),
                        Pair(binding.description, "description"),
                        Pair(binding.date, "date"),
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}