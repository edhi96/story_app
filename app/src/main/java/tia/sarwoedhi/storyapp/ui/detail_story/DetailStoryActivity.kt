package tia.sarwoedhi.storyapp.ui.detail_story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import tia.sarwoedhi.storyapp.R
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.databinding.ActivityDetailStoryBinding
import tia.sarwoedhi.storyapp.utils.Constant.EXTRA_STORY

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_story)
        setDetail()
    }

    private fun setDetail() {
        val detail = intent.getParcelableExtra<StoryEntity>(EXTRA_STORY)

        binding.apply {
            tvName.text = detail?.name
            tvDesc.text = detail?.description
            tvDate.text = detail?.createdAt
            Glide.with(this@DetailStoryActivity).load(detail?.photoUrl).into(ivStory)
        }
    }
}