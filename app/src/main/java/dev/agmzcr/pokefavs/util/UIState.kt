package dev.agmzcr.pokefavs.util

sealed class UIState {
    object Loading: UIState()
    data class Error(val errorMessage: String?): UIState()
    data class Success<T>(val content: T): UIState()
}
