package tia.sarwoedhi.storyapp.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.databinding.ActivitySplashBinding
import tia.sarwoedhi.storyapp.ui.login.LoginActivity
import tia.sarwoedhi.storyapp.ui.main.MainActivity
import tia.sarwoedhi.storyapp.utils.UiState

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(4500)
            withContext(Dispatchers.Main) {
                splashViewModel.getToken().observe(this@SplashActivity, ::splashResponse)
            }
        }

    }

    private fun splashResponse(resource: UiState<String>?) {
        when (resource) {
            is UiState.Success -> {
                MainActivity.newInstance(this)
            }
            is UiState.Error -> {
                LoginActivity.newInstance(this)
            }
            is UiState.Loading -> {

            }
            else -> {}
        }
    }

}