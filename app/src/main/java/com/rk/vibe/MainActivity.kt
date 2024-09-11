package com.rk.vibe

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rk.vibe.data.MainViewModel
import com.rk.vibe.data.getSongs
import com.rk.vibe.service.MusicService
import com.rk.vibe.ui.theme.VibeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    lateinit var viewModel: MainViewModel
    private var musicService: MusicService? = null
    private var isBound = false
    
    
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }
    
    override fun onStart() {
        super.onStart()
        // Bind to MusicService
        Intent(this, MusicService::class.java).also { intent ->
            startService(intent)  // Start service explicitly to run in foreground
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }
    
    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
    
    fun playMusic(uri: Uri) {
        if (isBound) {
            musicService?.playMusic(uri)
        }
    }
    
    fun stopMusic() {
        if (isBound) {
            musicService?.stopMusic()
        }
    }
    fun isPlaying() : Boolean{
        if (isBound) {
            musicService?.isPlaying()
        }
        return false
    }
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MusicService.CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        lifecycleScope.launch(Dispatchers.IO){
            if (viewModel.songs.value.isEmpty()) {
                viewModel.updateSongs(getSongs(this@MainActivity))
            }
        }
        

        setContent {
            VibeTheme {
                MainScreen(mainActivity = this)
            }
        }
    }
}