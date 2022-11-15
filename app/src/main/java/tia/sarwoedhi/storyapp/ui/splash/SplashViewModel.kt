package tia.sarwoedhi.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tia.sarwoedhi.storyapp.domain.use_case.GetTokenUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val getTokenUseCase: GetTokenUseCase) : ViewModel() {

    fun getToken(): LiveData<UiState<String>> {
        return getTokenUseCase.invoke().asLiveData()
    }

}