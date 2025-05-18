package com.example.qloq.presentation

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.qloq.R
import java.util.*
import kotlin.concurrent.schedule

//This module renders the pet interactions screen
@Composable
fun RenderPet(
    swipeDismissableNavController: NavController,
    ePet: EPet,
    sharedPreferences: SharedPreferences
) {

    var toggleAnim by remember { mutableStateOf(false) }

    fun fadeAnim() {
        toggleAnim = !toggleAnim
        Timer().schedule(750) {
            toggleAnim = !toggleAnim
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7727D)),
        verticalArrangement = Arrangement.Center
    ) {

        Box(Modifier.fillMaxSize()) {
            Image(
                // Background Image
                painterResource(R.drawable.bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HappinessIndicator(ePet)
                DetailsButtonComposable(swipeDismissableNavController)
            }
            Image(
                // Sprite Image
                painterResource(R.drawable.pink_monster),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 56.dp)
                    .size(50.dp),
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 43.dp, bottom = 43.dp)
            ) {
                AnimatedVisibility(
                    visible = toggleAnim,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Box {
                        Icon(
                            painterResource(R.drawable.bubble),
                            contentDescription = null,
                            modifier = Modifier
                                .size(45.dp),
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp)
                        ) {
                            Icon(
                                // Response image animation
                                painterResource(R.drawable.heart_solid),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(23.dp),
                                tint = Color(0xFFE91E3C),
                            )
                        }
                    }
                }
            }

            Row(
                // Row that contains all interactions
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    // Hunger Button
                    onClick = {
                        fadeAnim()
                        ePet.incHunger()

                        val editor = sharedPreferences.edit()
                        editor.putInt("hungerStat", ePet.hunger)
                        editor.apply()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.burger_solid),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(16.dp),
                        tint = Color(0xFFA7727D)
                    )
                }
                OutlinedButton(
                    //Thirst Button
                    onClick = {
                        fadeAnim()
                        ePet.incThirst()

                        val editor = sharedPreferences.edit()
                        editor.putInt("thirstStat", ePet.thirst)
                        editor.apply()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.mug_hot_solid),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(16.dp),
                        tint = Color(0xFFA7727D)
                    )
                }
                OutlinedButton(
                    //Happiness Button
                    onClick = {
                        fadeAnim()
                        ePet.incHappiness()

                        val editor = sharedPreferences.edit()
                        editor.putInt("happinessStat", ePet.happiness)
                        editor.apply()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.hand_regular),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(16.dp),
                        tint = Color(0xFFA7727D)
                    )
                }
            }
        }
    }
}


/**
 * Accepts[ePet] as param
 * Calculates and shows the current state of the pet
 * */
@Composable
fun HappinessIndicator(ePet: EPet) {
    val avgHealth = ePet.getAverage()
    Box(
        modifier = Modifier
            .size(35.dp)
            .background(Color.White , CircleShape),
    ) {
        if (avgHealth > 60) {
            Icon(
                painter = painterResource(id = R.drawable.face_laugh_squint_regular),
                contentDescription = "Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp),
                tint = Color(0xFF4CAF50)
            )
        } else if (avgHealth > 35) {
            Icon(
                painter = painterResource(id = R.drawable.face_meh_regular),
                contentDescription = "Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp),
                tint = Color(0xFFFF5722)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.face_sad_cry_regular),
                contentDescription = "Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp),
                tint = Color(0xFFCC0C1C)
            )
        }
    }
}


/**
 * Accepts[NavController] as param
 * Shows details button and navigates to details screen
 * */
@Composable
fun DetailsButtonComposable(swipeDismissableNavController: NavController) {
    OutlinedButton(
        onClick = { swipeDismissableNavController.navigate("DetailsScreen") },
        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
        modifier = Modifier.size(35.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bars_solid),
            contentDescription = "Image",
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center),
            tint = Color(0xFFA7727D)
        )
    }

}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
//Enables preview of the Home screen in the Default Preview Android Studio window
@Composable
fun PetPreview() {
    val swipeDismissableNavController = rememberSwipeDismissableNavController()
    val context = LocalContext.current
    val sprite = EPet("Qlo", 45, 45, 45)

    RenderPet(
        swipeDismissableNavController,
        sprite,
        PreferenceManager.getDefaultSharedPreferences(context)
    )

}