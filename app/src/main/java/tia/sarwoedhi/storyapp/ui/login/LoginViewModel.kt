package tia.sarwoedhi.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.domain.use_case.LoginUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginState: MutableStateFlow<UiState<LoginResponse>> = MutableStateFlow(UiState.Loading)
    val loginState: StateFlow<UiState<LoginResponse>> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = UiState.Loading
            ).collect{
                _loginState.value = it
            }
        }

    }

}