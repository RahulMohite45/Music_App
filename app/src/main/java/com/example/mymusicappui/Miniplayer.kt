package com.example.mymusicappui
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.mymusicappui.ui.theme.Song
/*
@Composable
fun MiniPlayer(
    currentSong: Song?,
    isPlaying: Boolean,
    onPlayPause:  () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = currentSong != null,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        currentSong?.let { song ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2B45)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = song.imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = onPlayPause) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = onStop) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

class MusicPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null
    private var isPlaying: Boolean = false

    fun playPause(context: Context, song: Song, onStateChanged: (Boolean, Song?) -> Unit) {
        try {
            if (currentSong == song && mediaPlayer != null) {
                // Same song - toggle play/pause
                mediaPlayer?.let { player ->
                    if (isPlaying) {
                        player.pause()
                        isPlaying = false
                    } else {
                        player.start()
                        isPlaying = true
                    }
                    onStateChanged(isPlaying, currentSong)
                }
            } else {
                // Different song - start new playback
                stop()

                mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, Uri.parse("android.resource://${context.packageName}/${song.rawResId}"))
                    prepareAsync()
                    setOnPreparedListener {
                        start()
                        this@MusicPlayerManager.isPlaying = true
                        currentSong = song
                        onStateChanged(isPlaying, currentSong)
                    }
                    setOnCompletionListener {
                        this@MusicPlayerManager.isPlaying = false
                        currentSong = null
                        onStateChanged(isPlaying, currentSong)
                    }
                    setOnErrorListener { _, what, extra ->
                        Log.e("MusicPlayer", "Error: $what, $extra")
                        this@MusicPlayerManager.isPlaying = false
                        currentSong = null
                        onStateChanged(isPlaying, currentSong)
                        true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error: ${e.message}")
        }
    }

    fun stop() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
        isPlaying = false
        currentSong = null
    }

    fun release() {
        stop()
    }

    fun getCurrentState(): Pair<Boolean, Song?> {
        return Pair(isPlaying, currentSong)
    }
}*/