package com.example.mymusicappui

import android.content.Context
import android.provider.MediaStore
import com.example.mymusicappui.MusicData

object MusicRepository {

    fun fetchSongs(context: Context): List<MusicData> {
        val musicList = mutableListOf<MusicData>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val pathIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex) ?: "Unknown Title"
                val artist = it.getString(artistIndex) ?: "Unknown Artist"
                val path = it.getString(pathIndex) ?: ""

                musicList.add(MusicData(title, artist, path))
            }
        }

        return musicList
    }
}