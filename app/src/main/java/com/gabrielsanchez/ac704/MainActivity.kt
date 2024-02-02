package com.gabrielsanchez.ac704

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getSystemService
import com.gabrielsanchez.ac704.ui.theme.AC704Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AC704Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val initialColor = Color.Red
                    var backgroundColor by remember { mutableStateOf(initialColor) }
                    var sensorValue by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = backgroundColor),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center

                    ) {

                        LatigoContainer(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor),
                            context = LocalContext.current,
                            onColorChange = { newColor -> backgroundColor = newColor },
                            onSensorChange = { newSensorValue -> sensorValue = newSensorValue }
                        )

                        Text(text = sensorValue)

                    }
                }
            }
        }
    }
}

@Composable
fun LatigoContainer(modifier: Modifier, context: Context, onColorChange: (Color) -> Unit, onSensorChange: (String) -> Unit) {
    var sm: SensorManager? = null
    var sa: Sensor? = null
    var latigo = 0
    var SEL: SensorEventListener? = null

    LaunchedEffect(context) {
        sm = getSystemService(context, SensorManager::class.java)
        sa = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        SEL = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                var x = event!!.values[0]
                onSensorChange("Sensor value: $x")
                if (x < -5 && latigo == 0) {
                    latigo++
                    // Cambiar color a azul
                    onColorChange(Color.Blue)
                } else if (x > 5 && latigo == 1) {
                    latigo++

                }
                if (latigo == 2) {
                    sonido()
                    latigo = 0
                    onColorChange(Color.Green) // Change color on successful event
                }
            }

            fun sonido() {
                val mp: MediaPlayer = MediaPlayer.create(context, R.raw.latigo)
                mp.start()
            }
        }

        sm?.registerListener(SEL, sa, SensorManager.SENSOR_DELAY_NORMAL)

        onColorChange(Color.White)
    }
}