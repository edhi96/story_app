package tia.sarwoedhi.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tia.sarwoedhi.storyapp.R
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.databinding.ActivityMainBinding
import tia.sarwoedhi.storyapp.ui.add_story.AddStoryActivity
import tia.sarwoedhi.storyapp.ui.login.LoginActivity
import tia.sarwoedhi.storyapp.ui.main.adapter.LoadingStateAdapter
import tia.sarwoedhi.storyapp.ui.main.adapter.StoryAdapter
import tia.sarwoedhi.storyapp.ui.maps.MapsActivity
import tia.sarwoedhi.storyapp.utils.UiState

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        fun newInstance(ctx: Context) {
            val dataIntent = Intent(ctx, MainActivity::class.java)
            ctx.startActivity(dataIntent)
        }
    }

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.home)
        setupView()
        nextPage()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()
        mainViewModel.getListStory()
        mainViewModel.stories.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { resource ->
                storiesResponse(resource)
            }.cachedIn(lifecycleScope).launchIn(lifecycleScope)

        storyAdapter.addLoadStateListener { loadState ->
            with(binding) {
                if (loadState.mediator?.refresh !is LoadState.Loading) {
                    val error = when {
                        loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                        loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                        loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                        else -> null
                    }
                    error?.let {
                        if (storyAdapter.snapshot().isEmpty()) {
                            binding.tvMessage.visibility = View.VISIBLE
                            binding.tvMessage.text = it.error.localizedMessage
                        }
                    }
                }
            }
        }
        with(binding.rvStory) {
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun storiesResponse(resource: PagingData<StoryEntity>?) {
        resource?.let {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun nextPage() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                    .toBundle()
            )
        }

        binding.fabMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                    .toBundle()
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.alpha = if (isLoading) 1.0f else 0.0f
        binding.tvMessage.alpha = if (isLoading) 0.0f else 1.0f
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvMessage.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_logout) {
            showAlertDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val alert = builder.create()
        builder.setTitle(getString(R.string.log_out)).setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.no)) { _, _ ->
                alert.cancel()
            }
            .setNegativeButton(getString(R.string.yes)) { _, _ ->
                logout()
            }
            .show()
    }

    private fun logout() {
        mainViewModel.logOut()
        mainViewModel.loginState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { resource ->
                when (resource) {
                    is UiState.Success -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        Toast.makeText(this, resource.error, Toast.LENGTH_LONG).show()
                    }
                    is UiState.Loading -> {
                        showLoading(true)
                    }
                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }
}
