package tia.sarwoedhi.storyapp.ui.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tia.sarwoedhi.storyapp.databinding.ItemStoryBinding
import tia.sarwoedhi.storyapp.domain.entities.Story
import tia.sarwoedhi.storyapp.ui.detail_story.DetailStoryActivity
import tia.sarwoedhi.storyapp.utils.Constant.EXTRA_STORY

class StoryAdapter(private val listStory: MutableList<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView.context).load(story.image).into(binding.imageView)
                title.text = story.title
                description.text = story.description
                date.text = story.date
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
}