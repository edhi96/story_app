package tia.sarwoedhi.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.RegisterUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val registerUseCase: RegisterUseCase) :
    ViewModel() {

    fun register(
        email: String,
        password: String,
        name: String
    ): LiveData<UiState<BaseResponse>> {
        return registerUseCase(email, password, name = name).asLiveData()
    }

}