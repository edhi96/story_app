package tia.sarwoedhi.storyapp.utils

sealed class UiState<out R> private constructor() {
    data class Success<out T>(val data: T?) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}