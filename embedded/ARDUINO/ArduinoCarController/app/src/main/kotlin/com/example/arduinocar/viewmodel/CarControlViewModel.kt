package com.example.arduinocar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arduinocar.data.CarRepo
import kotlinx.coroutines.Dispatchers
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

    // Protect our server and make sure that we do 1 request at a time
    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    fun onMoveForward(state: Int) {
        viewModelScope.launch(dispatcher) {
            repo.onMoveForward(state)
        }
    }

    fun onMoveRight(state: Int) {
        viewModelScope.launch(dispatcher) {
            repo.onMoveRight(state)
        }
    }

    fun onMoveLeft(state: Int) {
        viewModelScope.launch(dispatcher) {
            repo.onMoveLeft(state)
        }
    }

    fun onMoveBackward(state: Int) {
        viewModelScope.launch(dispatcher) {
            repo.onMoveBackward(state)
        }
    }

    fun onStop() {
        viewModelScope.launch(dispatcher) {
            repo.onStop()
        }
    }

    fun onSpeedChanged(newValue: Int, state: Int) {
        repo.onSpeedChanged(newValue, state)
    }

    fun onFastMode(enabled: Boolean) {
        repo.onFastMode(enabled)
    }

    fun onSlowDown() {
        repo.onSlowDown()
    }
}
