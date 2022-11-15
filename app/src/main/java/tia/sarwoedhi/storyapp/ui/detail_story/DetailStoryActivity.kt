package tia.sarwoedhi.storyapp.ui.detail_story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import tia.sarwoedhi.storyapp.databinding.ActivityDetailStoryBinding
import tia.sarwoedhi.storyapp.domain.entities.Story
import tia.sarwoedhi.storyapp.utils.Constant.EXTRA_STORY

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Story"
        setDetail()
    }

    private fun setDetail() {
        val detail = intent.getParcelableExtra<Story>(EXTRA_STORY)

        binding.apply {
            tvName.text = detail?.title
            tvDesc.text = detail?.description
            tvDate.text = detail?.date
            Glide.with(this@DetailStoryActivity).load(detail?.image).into(ivStory)
        }
    }
}