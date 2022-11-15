package tia.sarwoedhi.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tia.sarwoedhi.storyapp.R
import tia.sarwoedhi.storyapp.databinding.ActivityMainBinding
import tia.sarwoedhi.storyapp.domain.entities.Story
import tia.sarwoedhi.storyapp.ui.add_story.AddStoryActivity
import tia.sarwoedhi.storyapp.ui.login.LoginActivity
import tia.sarwoedhi.storyapp.ui.main.adapter.StoryAdapter
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
    private val listStory = mutableListOf<Story>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
        setupView()
        nextPage()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setupView() {
        storyAdapter = StoryAdapter(listStory)
        CoroutineScope(Dispatchers.Main).launch {
            mainViewModel.getListStory().observe(this@MainActivity, ::storiesResponse)
        }
        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("hasil","pause!")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hasil","onResume!")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hasil","onStop!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hasil","Destroyed!")
    }


    private fun storiesResponse(resource: UiState<List<Story>>?) {
        when (resource) {
            is UiState.Success -> {
                showLoading(false)
                resource.data?.let { listStory.addAll(it) }
                storyAdapter.notifyDataSetChanged()
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
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        mainViewModel.logOut().observe(this) { resource ->
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
        }

    }
}