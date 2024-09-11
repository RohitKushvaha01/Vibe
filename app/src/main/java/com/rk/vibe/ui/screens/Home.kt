package com.rk.vibe.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rk.vibe.MainActivity
import com.rk.vibe.data.MainViewModel
import com.rk.vibe.data.Song

@Composable
fun Home(modifier: Modifier = Modifier, mainActivity: MainActivity) {
  val songs by mainActivity.viewModel.songs.collectAsState()
  
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    LazyColumn {
      items(items = songs, key = { song ->
        song.id
      }) { song ->
        SongItem(song,mainActivity)
      }
    }
    
  }
}

@Composable
fun SongItem(song: Song,mainActivity: MainActivity) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    onClick = {
      if (mainActivity.isPlaying()){
        mainActivity.stopMusic()
      }
      mainActivity.playMusic(Uri.parse(song.path))
      
    }
  ) {
    Row(
      modifier = Modifier.padding(8.dp)
    ) {
      AsyncImage(
        model = song.artwork,
        contentDescription = "Album Art",
        modifier = Modifier
          .size(64.dp)
          .padding(4.dp),
        contentScale = ContentScale.Crop
      )
      
      Spacer(modifier = Modifier.width(8.dp))
      
      Column(
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = song.name ?: "Unknown Title", style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = song.author ?: "Unknown Artist", style = MaterialTheme.typography.bodySmall
        )
      }
    }
  }
}
