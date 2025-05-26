package com.example.qloq.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import androidx.wear.compose.material.Icon
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.qloq.R
import com.example.qloq.presentation.theme.QLOQTheme
import java.lang.System.currentTimeMillis

//Initialisation class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

        // below line is to get to integer values from our shared prefs.
        // If not present set default to 30.
        val hungerStat = sharedPreferences.getInt("hungerStat", 30)
        val thirstStat = sharedPreferences.getInt("thirstStat", 30)
        val happinessStat = sharedPreferences.getInt("happinessStat", 30)

        //Update the EPet's statistics based on the last time they were seen.
        val ePet = EPet("Qlo", hungerStat, thirstStat, happinessStat)
        val lastTimeSeen = sharedPreferences.getLong("timeStamp", currentTimeMillis())
        ePet.updateStats(lastTimeSeen - currentTimeMillis())

        setContent {
            QloqApp(ePet, sharedPreferences)
        }
    }
}

@Composable
fun QloqApp(ePet: EPet, sharedPreferences: SharedPreferences) {
    QLOQTheme {
        val swipeDismissableNavController = rememberSwipeDismissableNavController()

        SwipeDismissableNavHost(
            navController = swipeDismissableNavController,
            startDestination = "Landing",
        ) {
            composable("Landing") {
                Face()
//                RenderPet(swipeDismissableNavController, ePet, sharedPreferences)
            }
            composable("DetailsScreen") {
                DetailsScreen(ePet, sharedPreferences)
            }
        }
    }
}
@Composable
fun Face() {
    var expanded = remember { mutableStateOf(false) }

    // Mocked battery percentage
    val batteryLevel = 0.65f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (expanded.value) Color.Gray else Color.DarkGray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle",
                    tint = Color.White
                )
            }

            AnimatedVisibility(visible = expanded.value) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(3) {
                        IconButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.DarkGray, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star, // Replace with appropriate icons
                                contentDescription = "Action",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.happy2),
            contentDescription = "Image",
            modifier = Modifier
                .align(Alignment.Center)
                .size(186.dp)
                .padding(12.dp),
            tint = Color(0xFFA7727D)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp)) // Rounded for circular devices
                .background(Color.DarkGray)
        ) {
            LinearProgressIndicator(
                progress = batteryLevel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color.Green,
                trackColor = Color.Gray
            )
        }
    }
}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
//Enables Device specific previews in the Default Preview Android Studio window
@Composable
fun DefaultPreview() {
    val sprite = EPet("Qlo", 45, 45, 45)
    val context = LocalContext.current
    QloqApp(sprite, PreferenceManager.getDefaultSharedPreferences(context))
}