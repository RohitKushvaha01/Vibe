package com.rk.vibe

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.rk.vibe.data.MainViewModel
import com.rk.vibe.ui.screens.History
import com.rk.vibe.ui.screens.Home
import com.rk.vibe.ui.screens.Liked

@Composable
fun MainScreen(modifier: Modifier = Modifier, mainActivity: MainActivity) {
  var selectedItem by rememberSaveable {
    mutableIntStateOf(0)
  }
  val haptic = LocalHapticFeedback.current
  
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar {
        NavigationBarItem(
          label = { Text(text = "Home") },
          selected = selectedItem == 0, onClick = {
          selectedItem = 0
          haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }, icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") })
        NavigationBarItem(
          label = { Text(text = "History") },
          selected = selectedItem == 1,
          onClick = {
            selectedItem = 1
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          },
          icon = {
            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "History")
          },
        )
        NavigationBarItem(
          label = { Text(text = "Liked") },
          selected = selectedItem == 2,
          onClick = {
            selectedItem = 2
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          },
          icon = {
            Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Liked")
          },
        )
      }
    }
  ) { innerPadding ->
    ContentScreen(
      modifier = Modifier.padding(innerPadding),
      selectedIndex = selectedItem,
      mainActivity = mainActivity
    )
  }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, mainActivity: MainActivity) {
  Crossfade(targetState = selectedIndex, label = "screens") { screen ->
    when (screen) {
      0 -> Home(modifier = modifier,mainActivity)
      1 -> History(modifier = modifier)
      2 -> Liked(modifier = modifier)
    }
  }
}
