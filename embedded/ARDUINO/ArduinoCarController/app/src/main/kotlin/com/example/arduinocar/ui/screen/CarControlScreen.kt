package com.example.arduinocar.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.arduinocar.viewmodel.CarControl

@Composable
fun CarRemoteControl(
    onRight: (Int) -> Unit,
    onLeft: (Int) -> Unit,
    onTop: (Int) -> Unit,
    onBottom: (Int) -> Unit,
    onStop: () -> Unit,
    onSlowDown: () -> Unit,
    onSpeedChanged: (Int, Int) -> Unit,
    onFastMode: (Boolean) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center
        ) {

            // First row

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CarSkeletonCanvas(driving = true, speed = 1.0f)
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Forward Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowUp, "Forward",
                        onPress = { onTop(CarControl.PRESSED) },
                        onRelease = { onTop(CarControl.RELEASED) })
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Forward Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowUp, "Forward",
                        onPress = { onTop(CarControl.PRESSED) },
                        onRelease = { onTop(CarControl.RELEASED) })
                }

            }


            // Second row

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Backward Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowDown, "Backward",
                        onPress = { onBottom(CarControl.PRESSED) },
                        onRelease = { onBottom(CarControl.RELEASED) })

                    // Left Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowLeft, "Left",
                        onPress = { onLeft(CarControl.PRESSED) },
                        onRelease = { onLeft(CarControl.RELEASED) })

                    // Stop Button
                    DirectionButton(
                        Icons.Default.Stop, "Stop",
                        onPress = { onStop() })
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Stop Button
                    DirectionButton(
                        Icons.Default.Stop, "Stop",
                        onPress = { onStop() })

                    // Right Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowRight, "Right",
                        onPress = { onRight(CarControl.PRESSED) },
                        onRelease = { onRight(CarControl.RELEASED) })

                    // Backward Button
                    DirectionButton(
                        Icons.Default.KeyboardArrowDown, "Reverse",
                        onPress = { onBottom(CarControl.PRESSED) },
                        onRelease = { onBottom(CarControl.RELEASED) })

                }
            }

            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IntegerInput { newValue ->
                    onSpeedChanged(newValue, CarControl.MOVE_SPEED)
                }
            }

            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IntegerInput { newValue ->
                    onSpeedChanged(newValue, CarControl.TURN_SPEED)
                }
            }

        }
    }
}

@Composable
fun MyToggle(
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var initial by remember { mutableStateOf(isChecked) }

    Switch(checked = initial, onCheckedChange = onToggle)
}

@Composable
fun IntegerInput(label: String = "Enter speed", onChange: (Int) -> Unit) {

    var text by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = { newText ->
            text = newText
            if (newText.isNotEmpty()) {
                onChange(newText.toInt())
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun DirectionButton(
    icon: ImageVector,
    label: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    Button(
        onClick = { onPress() },
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(40.dp))
    }
}
