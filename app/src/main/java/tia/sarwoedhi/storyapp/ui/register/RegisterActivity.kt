package tia.sarwoedhi.storyapp.ui.register

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
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.databinding.ActivityRegisterBinding
import tia.sarwoedhi.storyapp.ui.login.LoginActivity
import tia.sarwoedhi.storyapp.utils.UiState

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    companion object {
        fun newInstance(ctx: Context) {
            val dataIntent = Intent(ctx, RegisterActivity::class.java)
            ctx.startActivity(dataIntent)
        }
    }

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
        register()
    }


    private fun register() {
        var email = binding.emailEditText.text.toString()
        var password = binding.passwordEditText.text.toString()
        var name = binding.nameEditText.text.toString()
        setMyButtonEnable(email, password,name)

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = p0.toString()
                setMyButtonEnable(email, password,name)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = p0.toString()
                setMyButtonEnable(email, password,name)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = p0.toString()
                setMyButtonEnable(email, password,name)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.btnRegister.setOnClickListener {
            registerViewModel.register(email, password, name)
            registerViewModel.registerState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { registerResponse(it) }
                .launchIn(lifecycleScope)
        }

        binding.haveAccount.setOnClickListener {
            LoginActivity.newInstance(this)
        }

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

    private fun registerResponse(resource: UiState<BaseResponse>?) {
        when (resource) {
            is UiState.Success -> {
                loadingState(false)
                nextPage()
            }
            is UiState.Error -> {
                loadingState(false)
                Toast.makeText(this, resource.error, Toast.LENGTH_LONG).show()
            }
            is UiState.Loading -> {
                loadingState(true)
            }
            else -> {}
        }
    }

    private fun nextPage() {
        LoginActivity.newInstance(this)
        finishAffinity()
    }

    private fun playAnimation() {

        val titleTextView =
            ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)

        val nameTextView = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val emailTextView = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val haveAccount =
            ObjectAnimator.ofFloat(binding.haveAccount, View.ALPHA, 1f).setDuration(500)

        val signupButton =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                haveAccount,
                signupButton
            )
            //startDelay = 600
            start()
        }

    }

    private fun setMyButtonEnable(emailString: String, password: String, name: String) {
        binding.btnRegister.isEnabled =
            emailString.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()
    }

    private fun loadingState(state: Boolean) {
        binding.progressBar.alpha = if (state) 1.0F else 0.0F
        binding.btnRegister.alpha = if (!state) 1.0F else 0.0F
    }
}