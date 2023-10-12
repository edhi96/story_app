package tia.sarwoedhi.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.databinding.ActivityLoginBinding
import tia.sarwoedhi.storyapp.ui.main.MainActivity
import tia.sarwoedhi.storyapp.ui.register.RegisterActivity
import tia.sarwoedhi.storyapp.utils.UiState

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        fun newInstance(ctx: Context) {
            val dataIntent = Intent(ctx, LoginActivity::class.java)
            ctx.startActivity(dataIntent)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
        login()
    }

    private fun login() {
        var email = binding.emailEditText.text.toString()
        var password = binding.passwordEditText.text.toString()
        setMyButtonEnable(email, password)

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = p0.toString()
                setMyButtonEnable(email, password)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = p0.toString()
                setMyButtonEnable(email, password)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnLogin.setOnClickListener {
            loginViewModel.login(email, password)
            loginViewModel.loginState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { loginResponse(it) }
                .launchIn(lifecycleScope)
        }

        binding.dontHaveAccount.setOnClickListener {
            RegisterActivity.newInstance(this)
        }

    }

    private fun setMyButtonEnable(emailString: String, password: String) {
        binding.btnLogin.isEnabled = emailString.isNotEmpty() && password.isNotEmpty()
    }

    private fun loginResponse(resource: UiState<LoginResponse>?) {
        when (resource) {
            is UiState.Success -> {
                loadingState(false)
                nextPage()
            }
            is UiState.Error -> {
                Toast.makeText(this@LoginActivity, resource.error, Toast.LENGTH_LONG).show()
                loadingState(false)
            }
            is UiState.Loading -> {
                loadingState(true)
            }
            else -> {}
        }
    }

    private fun nextPage() {
        MainActivity.newInstance(this)
        finishAffinity()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {

        val titleTextView = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        val emailTextView = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1.5f).setDuration(500)

        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val dontHaveAccount =
            ObjectAnimator.ofFloat(binding.dontHaveAccount, View.ALPHA, 1f).setDuration(500)

        val signupButton = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                dontHaveAccount,
                signupButton
            )
            startDelay = 500
            start()
        }

    }

    private fun loadingState(state: Boolean) {
        binding.progressBar.alpha = if(state) 1.0F else 0.0F
        binding.btnLogin.alpha  = if(!state) 1.0F else 0.0F
    }

}