package com.example.arduinocar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arduinocar.ui.screen.CarRemoteControl
import com.example.arduinocar.viewmodel.CarControlViewModel

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
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun MainUI() {

    val viewModel = viewModel<CarControlViewModel>()

    val onMoveForward: (Int) -> Unit = { viewModel.onMoveForward(it) }
    val onMoveRight: (Int) -> Unit = { viewModel.onMoveRight(it) }
    val onMoveLeft: (Int) -> Unit = { viewModel.onMoveLeft(it) }
    val onMoveBackward: (Int) -> Unit = { viewModel.onMoveBackward(it) }
    val onStop: () -> Unit = { viewModel.onStop() }
    val onSlowDown: () -> Unit = { viewModel.onSlowDown() }

    val onSpeedChanged: (Int, Int) -> Unit = { val1, val2 -> viewModel.onSpeedChanged(val1, val2) }
    val onFastMode: (Boolean) -> Unit = { viewModel.onFastMode(it) }

    CarRemoteControl(
        onRight = onMoveRight,
        onLeft = onMoveLeft,
        onBottom = onMoveBackward,
        onTop = onMoveForward,
        onStop = onStop,
        onSpeedChanged = onSpeedChanged,
        onFastMode = onFastMode,
        onSlowDown = onSlowDown,
    )

}
