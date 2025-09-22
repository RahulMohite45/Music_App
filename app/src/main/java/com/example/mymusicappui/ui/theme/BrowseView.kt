package com.example.mymusicappui.ui.theme

import android.content.pm.PackageManager
//import androidx.compose.material.icons.Icons

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.input.pointer.consume
import androidx.compose.foundation.Canvas
//edited
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.detectDragGestures
import android.os.Build
import android.util.Log
import androidx.compose.material3.Card
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusicappui.MainViewModel
import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
//import androidx.compose.material.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.getValue
//import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.mymusicappui.RawSong
import com.example.mymusicappui.RawSongItem
import com.example.mymusicappui.rawSongs
import java.util.UUID
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
//edited
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Slider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

//import com.example.mymusicappui.model.RawSong

// Updated Browse Composable with Play/Stop and Like functionality
// Updated Browse Composable with Play/Stop and Like functionality
// Updated Browse Composable with Play/Stop and Like functionality

@Composable
fun WaveformProgressBar(
    progress: Float, // 0f to 1f
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    waveformColor: Color = Color.White.copy(alpha = 0.3f),
    progressColor: Color = Color(0xFF8B5CF6),
    barCount: Int = 60,
    minBarHeight: Dp = 4.dp,
    maxBarHeight: Dp = 32.dp,
    barWidth: Dp = 3.dp,
    barSpacing: Dp = 2.dp
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }

    // Generate random heights for waveform bars (in real app, this would be actual audio data)
    val barHeights = remember {
        (0 until barCount).map {
            Random.nextFloat() * 0.8f + 0.2f // Heights between 0.2 and 1.0
        }
    }

    Canvas(
        modifier = modifier
            .height(maxBarHeight)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                        dragProgress = newProgress
                    },
                    onDrag = { change, _ ->
                        val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        dragProgress = newProgress
                    },
                    onDragEnd = {
                        isDragging = false
                        onSeek(dragProgress)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                    onSeek(newProgress)
                }
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val totalBarWidth = barWidth.toPx()
        val totalSpacing = barSpacing.toPx()
        val totalWidth = (totalBarWidth + totalSpacing) * barCount - totalSpacing
        val startX = (canvasWidth - totalWidth) / 2f

        val currentProgress = if (isDragging) dragProgress else progress
        val progressX = startX + (totalWidth * currentProgress)

        barHeights.forEachIndexed { index, heightRatio ->
            val barHeight = minBarHeight.toPx() + (maxBarHeight.toPx() - minBarHeight.toPx()) * heightRatio
            val x = startX + index * (totalBarWidth + totalSpacing)
            val y = (canvasHeight - barHeight) / 2f

            // Determine if this bar is in the progress area
            val barColor = if (x <= progressX) progressColor else waveformColor

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(totalBarWidth, barHeight),
                cornerRadius = CornerRadius(totalBarWidth / 2f, totalBarWidth / 2f)
            )
        }
    }
}




@Composable
fun Browse(
    viewModel: MainViewModel = viewModel(),
    navController: NavController? = null
) {
    val songs = viewModel.rawSongs
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    val likedSongs = remember { mutableStateOf(setOf<String>()) }

    // Load raw songs when composable is first created
    LaunchedEffect(Unit) {
        viewModel.loadRawSongs(context)
        // Load liked songs from preferences
        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        val savedLikes = prefs.getStringSet("liked_songs", emptySet()) ?: emptySet()
        likedSongs.value = savedLikes
    }

    // Function to toggle like
    fun toggleLike(song: RawSong) {
        val currentLikes = likedSongs.value.toMutableSet()
        if (currentLikes.contains(song.id)) {
            currentLikes.remove(song.id)
        } else {
            currentLikes.add(song.id)
        }
        likedSongs.value = currentLikes

        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("liked_songs", currentLikes).apply()

        val message = if (currentLikes.contains(song.id)) "Added to favorites" else "Removed from favorites"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Filter songs based on search query
    val filteredSongs = remember(songs, searchQuery.value) {
        if (searchQuery.value.isBlank()) {
            songs
        } else {
            songs.filter { song ->
                song.title.contains(searchQuery.value, ignoreCase = true) ||
                        song.artist.contains(searchQuery.value, ignoreCase = true)
            }
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1B2F), Color(0xFF3C3B54))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(16.dp)
    ) {
        // Search Field
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            placeholder = { Text("Search Song", color = Color.White) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF2D2B45),
                unfocusedContainerColor = Color(0xFF2D2B45),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (songs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading songs...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // Recently Played Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RECENTLY PLAYED", color = Color.White, fontWeight = FontWeight.Bold)
                Text("See All", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(songs.take(5)) { song ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            // Navigate to music player screen
                            val songIndex = songs.indexOf(song)
                            Log.d("Browse", "Navigating to music player for song: ${song.title} at index $songIndex")
                            try {
                                // Pass the song index as a route parameter
                                navController?.navigate("music_player_screen/$songIndex") {
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                Log.e("Browse", "Navigation error: ${e.message}")
                                Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = song.imageResId),
                                contentDescription = "${song.title} album cover",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            song.title,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(140.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            song.artist,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(140.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recommendations Section
            Text("RECOMMENDATIONS", style = MaterialTheme.typography.titleMedium, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredSongs.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredSongs) { song ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navigate to music player screen
                                    val songIndex = songs.indexOf(song)
                                    Log.d("Browse", "Navigating to music player for song: ${song.title} at index $songIndex")
                                    try {
                                        navController?.navigate("music_player_screen/$songIndex") {
                                            launchSingleTop = true
                                        }
                                    } catch (e: Exception) {
                                        Log.e("Browse", "Navigation error: ${e.message}")
                                        Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                            colors = cardColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = song.imageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        song.title,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        song.artist,
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    song.duration,
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Play button
                                IconButton(
                                    onClick = {
                                        val songIndex = songs.indexOf(song)
                                        navController?.navigate("music_player_screen/$songIndex") {
                                            launchSingleTop = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White
                                    )
                                }

                                // Like button
                                IconButton(
                                    onClick = { toggleLike(song) }
                                ) {
                                    Icon(
                                        imageVector = if (likedSongs.value.contains(song.id))
                                            Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = if (likedSongs.value.contains(song.id))
                                            "Unlike" else "Like",
                                        tint = if (likedSongs.value.contains(song.id))
                                            Color.Red else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No songs match your search",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun MusicPlayerScreen(
    songIndex: Int,
    viewModel: MainViewModel = viewModel(),
    navController: NavController? = null
) {
    val context = LocalContext.current
    val songs by remember { derivedStateOf { viewModel.rawSongs } }

    // Music player states
    val isPlaying = remember { mutableStateOf(false) }
    val currentSong = remember { mutableStateOf<RawSong?>(null) }
    val currentSongIndex = remember { mutableStateOf(songIndex) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    val likedSongs = remember { mutableStateOf(setOf<String>()) }
    val currentPosition = remember { mutableStateOf(0) }
    val duration = remember { mutableStateOf(0) }
    val isUserSeeking = remember { mutableStateOf(false) }
    val isInitialized = remember { mutableStateOf(false) }

    fun playNext(
        songList: List<RawSong>,
        currentIndex: MutableState<Int>,
        currentSongState: MutableState<RawSong?>,
        context: Context,
        player: MutableState<MediaPlayer?>,
        playing: MutableState<Boolean>,
        durationState: MutableState<Int>,
        positionState: MutableState<Int>
    ) {
        if (songList.isNotEmpty()) {
            val nextIndex = (currentIndex.value + 1) % songList.size
            currentIndex.value = nextIndex
            val nextSong = songList[nextIndex]
            currentSongState.value = nextSong
            playNewSong(nextSong, context, player, playing, durationState, positionState) {
                playNext(songList, currentIndex, currentSongState, context, player, playing, durationState, positionState)
            }
        }
    }

    // Load songs and initialize player
    LaunchedEffect(Unit) {
        Log.d("MusicPlayer", "Loading songs and preferences")
        viewModel.loadRawSongs(context)
        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        val savedLikes = prefs.getStringSet("liked_songs", emptySet()) ?: emptySet()
        likedSongs.value = savedLikes
    }

    // Initialize current song when songs are loaded
    LaunchedEffect(songs) {
        Log.d("MusicPlayer", "Songs loaded: ${songs.size}")
        if (songs.isNotEmpty() && songIndex < songs.size && !isInitialized.value) {
            Log.d("MusicPlayer", "Setting current song at index $songIndex")
            currentSong.value = songs[songIndex]
            currentSongIndex.value = songIndex
            isInitialized.value = true
        }
    }

    // Auto-play when current song is set
    LaunchedEffect(currentSong.value) {
        currentSong.value?.let { song ->
            Log.d("MusicPlayer", "Auto-playing song: ${song.title}")
            playNewSong(song, context, mediaPlayer, isPlaying, duration, currentPosition) {
                playNext(songs, currentSongIndex, currentSong, context, mediaPlayer, isPlaying, duration, currentPosition)
            }
        }
    }

    // Update progress periodically
    LaunchedEffect(isPlaying.value, mediaPlayer.value) {
        while (isPlaying.value && !isUserSeeking.value) {
            delay(1000)
            mediaPlayer.value?.let { player ->
                try {
                    if (player.isPlaying) {
                        currentPosition.value = player.currentPosition
                        if (duration.value == 0) {
                            duration.value = player.duration
                        } else {

                        }
                    } else {

                    }
                } catch (e: Exception) {
                    Log.e("MusicPlayer", "Error updating progress: ${e.message}")
                }
            }
        }
    }

    // Clean up media player when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            Log.d("MusicPlayer", "Disposing media player")
            mediaPlayer.value?.release()
            mediaPlayer.value = null
        }
    }



    fun playPrevious() {
        if (songs.isNotEmpty()) {
            val prevIndex = if (currentSongIndex.value - 1 < 0) songs.size - 1 else currentSongIndex.value - 1
            currentSongIndex.value = prevIndex
            val prevSong = songs[prevIndex]
            currentSong.value = prevSong
            playNewSong(prevSong, context, mediaPlayer, isPlaying, duration, currentPosition) {
                playNext(songs, currentSongIndex, currentSong, context, mediaPlayer, isPlaying, duration, currentPosition)
            }
        }
    }

    fun togglePlayback() {
        mediaPlayer.value?.let { player ->
            try {
                if (isPlaying.value) {
                    player.pause()
                    isPlaying.value = false
                    Log.d("MusicPlayer", "Paused playback")
                } else {
                    player.start()
                    isPlaying.value = true
                    Log.d("MusicPlayer", "Resumed playback")
                }
            } catch (e: Exception) {
                Log.e("MusicPlayer", "Error toggling playback: ${e.message}")
                Toast.makeText(context, "Playback error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer.value?.let { player ->
            try {
                player.seekTo(position)
                currentPosition.value = position
                Log.d("MusicPlayer", "Seeked to position: $position")
            } catch (e: Exception) {
                Log.e("MusicPlayer", "Error seeking: ${e.message}")
            }
        }
    }

    fun toggleLike(song: RawSong) {
        val currentLikes = likedSongs.value.toMutableSet()
        if (currentLikes.contains(song.id)) {
            currentLikes.remove(song.id)
        } else {
            currentLikes.add(song.id)
        }
        likedSongs.value = currentLikes

        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("liked_songs", currentLikes).apply()

        val message = if (currentLikes.contains(song.id)) "Added to favorites" else "Removed from favorites"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Show loading state if songs aren't loaded yet
    if (songs.isEmpty() || currentSong.value == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1a1a2e),
                            Color(0xFF16213e),
                            Color(0xFF0f3460)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color(0xFF8B5CF6))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading music player...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } else {
        currentSong.value?.let { song ->
            FullScreenMusicPlayer(
                song = song,
                isPlaying = isPlaying.value,
                currentPosition = currentPosition.value,
                duration = duration.value,
                isLiked = likedSongs.value.contains(song.id),
                onPlayPause = { togglePlayback() },
                onNext = {
                    playNext(songs, currentSongIndex, currentSong, context, mediaPlayer, isPlaying, duration, currentPosition)
                },
                onPrevious = { playPrevious() },
                onSeek = { position ->
                    isUserSeeking.value = true
                    seekTo(position)
                    isUserSeeking.value = false
                },
                onLike = { toggleLike(song) },
                onClose = { navController?.popBackStack() }
            )
        }
    }
}

// Helper function to play a new song
private fun playNewSong(
    song: RawSong,
    context: Context,
    mediaPlayer: MutableState<MediaPlayer?>,
    isPlaying: MutableState<Boolean>,
    duration: MutableState<Int>,
    currentPosition: MutableState<Int>,
    onComplete: () -> Unit
) {
    try {
        Log.d("MediaPlayer", "Preparing to play song: ${song.title}")

        // Release current player
        mediaPlayer.value?.release()
        mediaPlayer.value = null
        isPlaying.value = false
        currentPosition.value = 0
        duration.value = 0

        // Create new MediaPlayer instance
        val newPlayer = MediaPlayer().apply {
            try {
                // Reset states
                reset()

                // Construct the URI for the raw resource
                val uri = Uri.parse("android.resource://${context.packageName}/${song.rawResId}")
                Log.d("MediaPlayer", "Setting data source: $uri")
                setDataSource(context, uri)

                setOnPreparedListener { player ->
                    try {
                        Log.d("MediaPlayer", "MediaPlayer prepared, starting playback")
                        player.start()
                        isPlaying.value = true
                        duration.value = player.duration
                        currentPosition.value = 0
                        Log.d("MediaPlayer", "Song started: ${song.title}, Duration: ${player.duration}ms")
                    } catch (e: Exception) {
                        Log.e("MediaPlayer", "Error starting playback: ${e.message}")
                        isPlaying.value = false
                        Toast.makeText(context, "Error starting playback", Toast.LENGTH_SHORT).show()
                    }
                }

                setOnCompletionListener { _ ->
                    Log.d("MediaPlayer", "Song completed: ${song.title}")
                    isPlaying.value = false
                    currentPosition.value = 0
                    onComplete()
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayer", "MediaPlayer error: what=$what, extra=$extra for song: ${song.title}")
                    isPlaying.value = false
                    Toast.makeText(context, "Error playing ${song.title}", Toast.LENGTH_SHORT).show()
                    true // Return true to indicate error was handled
                }

                // Prepare the media player asynchronously
                Log.d("MediaPlayer", "Starting async prepare")
                prepareAsync()

            } catch (e: Exception) {
                Log.e("MediaPlayer", "Error setting up MediaPlayer: ${e.message}")
                release()
                throw e
            }
        }

        mediaPlayer.value = newPlayer

    } catch (e: Exception) {
        Log.e("MediaPlayer", "Error creating MediaPlayer for song: ${song.title}, Error: ${e.message}")
        isPlaying.value = false
        mediaPlayer.value?.release()
        mediaPlayer.value = null
        Toast.makeText(context, "Cannot play ${song.title}: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun FullScreenMusicPlayer(
    song: RawSong,
    isPlaying: Boolean,
    currentPosition: Int,
    duration: Int,
    isLiked: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Int) -> Unit,
    onLike: () -> Unit,
    onClose: () -> Unit
) {
    // Format time helper function
    fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1B2F),
                        Color(0xFF3C3B54),
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Now Playing",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                IconButton(onClick = { /* More options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(29.dp))

            // Album Art
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = song.imageResId),
                    contentDescription = "${song.title} album cover",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                // Vinyl record effect overlay
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Song Info
            Text(
                text = song.title,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = song.artist,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Custom Waveform Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                WaveformProgressBar(
                    progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f,
                    onSeek = { progress ->
                        onSeek((progress * duration).toInt())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    waveformColor = Color.White.copy(alpha = 0.3f),
                    progressColor = Color(0xFF8B5CF6),
                    barCount = 50,
                    minBarHeight = 6.dp,
                    maxBarHeight = 40.dp,
                    barWidth = 4.dp,
                    barSpacing = 3.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = formatTime(duration),
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle Button
                IconButton(onClick = { /* Shuffle logic */ }) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Previous Button
                IconButton(
                    onClick = onPrevious,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Play/Pause Button
                Card(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    colors = cardColors(containerColor = Color(0xFF8B5CF6))
                ) {
                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Next Button
                IconButton(
                    onClick = onNext,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Repeat Button
                IconButton(onClick = { /* Repeat logic */ }) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }

                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) Color.Red else Color.White.copy(alpha = 0.7f)
                    )
                }

                IconButton(onClick = { /* Add to playlist */ }) {
                    Icon(
                        imageVector = Icons.Default.PlaylistAdd,
                        contentDescription = "Add to Playlist",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Your existing data classes remain the same
data class RawSong(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val artist: String,
    val duration: String,
    val imageResId: Int,
    val rawResId: Int
)

@Composable
fun getLikedSongs(context: Context): Set<String> {
    val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
    return prefs.getStringSet("liked_songs", emptySet()) ?: emptySet()
}