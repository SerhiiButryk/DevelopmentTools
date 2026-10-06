package com.example.arduinocar.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arduinocar.data.CarRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object CarControl {
    const val PRESSED = 1
    const val RELEASED = 2
    const val MOVE_SPEED = 3
    const val TURN_SPEED = 4
}

/**
 * ViewModel for car controlling using MVVM pattern
 */
class CarControlViewModel(
    private val repo: CarRepo = CarRepo()
) : ViewModel() {

    @Immutable
    data class UiState(val state: String = "")

    private val _state = MutableStateFlow<UiState>(UiState())
    val state = _state.asStateFlow()

    fun onMoveForward(state: Int) {
        viewModelScope.launch {
            repo.onMoveForward(state)
        }
        updateState()
    }

//    fun onMoveRight(state: Int) {
//        viewModelScope.launch {
//            repo.onMoveRight(state)
//        }
//        updateState()
//    }
//
//    fun onMoveLeft(state: Int) {
//        viewModelScope.launch {
//            repo.onMoveLeft(state)
//        }
//        updateState()
//    }

    fun onMoveBackward(state: Int) {
        viewModelScope.launch {
            repo.onMoveBackward(state)
        }
        updateState()
    }

    fun onStop() {
        viewModelScope.launch {
            repo.onStop()
        }
        updateState()
    }

    fun onSpeedChanged(newValue: Int, state: Int) {
        repo.onSpeedChanged(newValue, state)
        updateState()
    }

    fun onFastMode(enabled: Boolean) {
        repo.onFastMode(enabled)
        updateState()
    }

    fun onSlowDown() {
        repo.onSlowDown()
        updateState()
    }

    private fun updateState() {
        viewModelScope.launch {
            repo.getBatteryStateInfo().collect {
                _state.emit(UiState(it))
            }
        }
    }
}
