package tia.sarwoedhi.storyapp.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.RegisterUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _registerState: MutableStateFlow<UiState<BaseResponse>> = MutableStateFlow(UiState.Loading)
    val registerState: StateFlow<UiState<BaseResponse>> get() = _registerState

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            registerUseCase(email, password, name).stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = UiState.Loading
            ).collect {
                _registerState.value = it
            }
        }
    }

}