package com.rk.vibe.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.rk.vibe.R

class MusicService : Service() {
  private lateinit var mediaPlayer: MediaPlayer
  private val binder = MusicBinder()
  
  override fun onCreate() {
    super.onCreate()
    // Initialize the media player here
    mediaPlayer = MediaPlayer()
  }
  
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    startForegroundService()
    return START_STICKY
  }
  
  
  override fun onBind(intent: Intent?): IBinder {
    return binder
  }
  
  override fun onDestroy() {
    mediaPlayer.release()  // Release resources when the service is destroyed
    super.onDestroy()
  }
  
  // Method to start foreground notification
  private fun startForegroundService() {
    val notification = createNotification()
    startForeground(NOTIFICATION_ID, notification)
  }
  
  // Custom binder to return to clients (activity)
  inner class MusicBinder : Binder() {
    fun getService(): MusicService = this@MusicService
  }
  
  // Create a notification for foreground service
  private fun createNotification(): Notification {
    val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Music Player")
      .setContentText("Playing music")
      .setSmallIcon(R.drawable.music_note)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
    
    return notificationBuilder.build()
  }
  
  // Function to play music
  fun playMusic(uri: Uri) {
    mediaPlayer.reset()
    mediaPlayer.setDataSource(this, uri)
    mediaPlayer.prepare()
    mediaPlayer.start()
  }
  
  // Function to stop music
  fun stopMusic() {
    if (mediaPlayer.isPlaying) {
      mediaPlayer.stop()
    }
  }
  fun isPlaying():Boolean{
    return mediaPlayer.isPlaying
  }
  
  companion object {
    const val CHANNEL_ID = "MusicServiceChannel"
    const val NOTIFICATION_ID = 1
  }
}
