package com.example.arduinocar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arduinocar.data.GyroSensorsService
import com.example.arduinocar.ui.CarControlViewModel
import com.example.arduinocar.ui.CarRemoteControl

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainUI()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        GyroSensorsService.register(this)
    }

    override fun onPause() {
        super.onPause()
        GyroSensorsService.unregister()
    }

}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun MainUI() {

    val viewModel = viewModel<CarControlViewModel>()

    val onMoveForward: (Int) -> Unit = { viewModel.onMoveForward(it) }
    val onMoveRight: (Int) -> Unit = { /*viewModel.onMoveRight(it)*/ }
    val onMoveLeft: (Int) -> Unit = { /*viewModel.onMoveLeft(it)*/ }
    val onMoveBackward: (Int) -> Unit = { viewModel.onMoveBackward(it) }
    val onStop: () -> Unit = { viewModel.onStop() }
    val onSlowDown: () -> Unit = { viewModel.onSlowDown() }

    val onSpeedChanged: (Int, Int) -> Unit = { val1, val2 -> viewModel.onSpeedChanged(val1, val2) }
    val onFastMode: (Boolean) -> Unit = { viewModel.onFastMode(it) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    CarRemoteControl(
        onRight = onMoveRight,
        onLeft = onMoveLeft,
        onBottom = onMoveBackward,
        onTop = onMoveForward,
        onStop = onStop,
        onSpeedChanged = onSpeedChanged,
        onFastMode = onFastMode,
        onSlowDown = onSlowDown,
        state = state,
    )

}
