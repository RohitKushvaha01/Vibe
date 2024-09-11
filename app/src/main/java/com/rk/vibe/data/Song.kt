package com.rk.vibe.data

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import java.io.File

data class Song(
  val name: String?,
  val author: String?,
  val artwork: Drawable?,
  val path: String,
  val id: Long
)

fun getSongs(context: Context): MutableList<Song> {
  val musicList = mutableListOf<Song>()
  val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.DATA // Path to the file
  )
  
  val cursor = context.contentResolver.query(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    projection,
    null,
    null,
    null
  )
  
  cursor?.use {
    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(idColumn)
      val title = cursor.getString(titleColumn)
      val artist = cursor.getString(artistColumn)
      val path = cursor.getString(dataColumn)
      
      // Filter out non-audio files or files from specific directories
      if (isValidAudioFile(path)) {
        val albumArt = getAlbumArtForAlbum(path, context)
        musicList.add(Song(title, artist, albumArt, path, id))
      }
    }
  }
  return musicList
}

private fun isValidAudioFile(filePath: String): Boolean {
  val file = File(filePath)
  // You can add more conditions to filter files if needed
  return file.exists()
}

private fun getAlbumArtForAlbum(filePath: String, context: Context): Drawable? {
  val retriever = MediaMetadataRetriever()
  return try {
    retriever.setDataSource(filePath)
    val albumArt = retriever.embeddedPicture
    if (albumArt != null) {
      val bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.size)
      BitmapDrawable(context.resources, bitmap) // Use 'this.resources' in an Activity
    } else {
      null
    }
  } catch (e: Exception) {
    null // Handle exceptions related to invalid files
  } finally {
    retriever.release()
  }
}
