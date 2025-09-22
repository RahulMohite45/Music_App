package com.example.mymusicappui

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusicappui.MusicRepository
import com.example.mymusicappui.MusicRepository.fetchSongs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.UUID


class MainViewModel : ViewModel() {
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.DrawerScreen.AddAccount)
    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // For device storage songs (MusicData)
    private val _songs = mutableStateListOf<MusicData>()
    val songs: List<MusicData> get() = _songs

    // For raw folder songs (RawSong)
    private val _rawSongs = mutableStateListOf<RawSong>()
    val rawSongs: List<RawSong> get() = _rawSongs

    // For playlists
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    // Liked songs state
    private val _likedSongs = MutableStateFlow<Set<String>>(emptySet())
    val likedSongs: StateFlow<Set<String>> = _likedSongs.asStateFlow()

    // Raw songs data
    private val predefinedRawSongs = listOf(
        RawSong("Uyi amma", "Amit Trivedi", "3:10", R.raw.song1, R.drawable.song1, R.raw.song11111new, thumb = ""),
        RawSong("Sad Song", "Sapne Bade", "4:10", R.raw.song2, R.drawable.song2, R.raw.song2, thumb = ""),
        RawSong("lofi Song", "lofium", "3:04", R.raw.song3, R.drawable.song3, R.raw.song3, thumb = ""),
        RawSong("Bade Ache Lagta ahe", "Amit Kumar", "3:48", R.raw.song4, R.drawable.song4, R.raw.song4, thumb = ""),
        RawSong("Pal Pal dil ke pass", "Kishor kumar", "5:26", R.raw.song5, R.drawable.song5, R.raw.song5, thumb = ""),
        RawSong("ye tuna ky kiya","Pritam ,Javed","5:04",R.raw.song6,R.drawable.song6new,R.raw.song6)
    )

    init {
        // Create default "Liked Songs" playlist when ViewModel is initialized
        createDefaultLikedPlaylist()
    }

    // Load device storage songs
    fun loadSongs(context: Context) {
        val songs = MusicRepository.fetchSongs(context)
        _songs.clear()
        _songs.addAll(songs)
    }

    // Updated loadRawSongs method in your MainViewModel
    fun loadRawSongs(context: Context) {
        // Updated raw songs data with consistent resource IDs
        val predefinedRawSongs = listOf(
            RawSong(
                title = "Uyi amma",
                artist = "Amit Trivedi",
                duration = "3:10",
                rawResId = R.raw.song11111new, // Use the actual audio file
                imageResId = R.drawable.song1new,
                thumb = "",
                resId = R.raw.song11111new,
            ),
            RawSong(
                title = "Sad Song",
                artist = "Sapne Bade",
                duration = "4:10",
                rawResId = R.raw.song2,
                imageResId = R.drawable.sadsong1new,
                thumb = "",
                resId = R.raw.song2,
            ),
            RawSong(
                title = "lofi Song",
                artist = "lofium",
                duration = "3:04",
                rawResId = R.raw.song3,
                imageResId = R.drawable.song3,
                thumb = "",
                resId = R.raw.song3,
            ),
            RawSong(
                title = "Bade Ache Lagta ahe",
                artist = "Amit Kumar",
                duration = "3:48",
                rawResId = R.raw.song4,
                imageResId = R.drawable.song4,
                thumb = "",
                resId = R.raw.song4,
            ),
            RawSong(
                title = "Pal Pal dil ke pass",
                artist = "Kishor kumar",
                duration = "5:26",
                rawResId = R.raw.song5,
                imageResId = R.drawable.song5,
                thumb = "",
                resId = R.raw.song5,
            ),
            RawSong(
                title = "ye tuna ky kiya",
                artist = "Pritam, Javed",
                duration = "5:04",
                rawResId = R.raw.song6,
                imageResId = R.drawable.song6new,
                thumb = "",
                resId = R.raw.song6
            )
        )

        _rawSongs.clear()
        _rawSongs.addAll(predefinedRawSongs)

        // Load liked songs from SharedPreferences
        loadLikedSongs(context)

        Log.d("MainViewModel", "Raw songs loaded: ${_rawSongs.size}")
        _rawSongs.forEach { song ->
            Log.d("MainViewModel", "Song: ${song.title}, Raw ID: ${song.rawResId}")
        }
    }

    // Load liked songs from SharedPreferences
    private fun loadLikedSongs(context: Context) {
        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        val savedLikes = prefs.getStringSet("liked_songs", emptySet()) ?: emptySet()
        _likedSongs.value = savedLikes

        // Update the liked songs playlist
        updateLikedSongsPlaylist()
    }

    // Create default "Liked Songs" playlist
    private fun createDefaultLikedPlaylist() {
        val likedPlaylist = Playlist(
            id = "liked_songs_playlist",
            name = "Liked Songs",
            description = "Your favorite songs",
            songs = emptyList(),
            coverImageResId = R.drawable.song1 // You can use a heart icon or any default image
        )
        _playlists.value = listOf(likedPlaylist)
    }

    // Function to toggle like status and update playlist
    fun toggleLike(context: Context, song: RawSong) {
        val currentLikes = _likedSongs.value.toMutableSet()

        if (currentLikes.contains(song.id)) {
            // Remove from liked
            currentLikes.remove(song.id)
        } else {
            // Add to liked
            currentLikes.add(song.id)
        }

        // Update the liked songs state
        _likedSongs.value = currentLikes

        // Save to SharedPreferences
        val prefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("liked_songs", currentLikes).apply()

        // Update the liked songs playlist
        updateLikedSongsPlaylist()

        // Show toast message
        val message = if (currentLikes.contains(song.id)) "Added to favorites" else "Removed from favorites"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Update the liked songs playlist with current liked songs
    private fun updateLikedSongsPlaylist() {
        val likedSongIds = _likedSongs.value
        val likedSongsList = _rawSongs.filter { song -> likedSongIds.contains(song.id) }

        _playlists.value = _playlists.value.map { playlist ->
            if (playlist.id == "liked_songs_playlist") {
                playlist.copy(songs = likedSongsList)
            } else {
                playlist
            }
        }
    }

    // Function to create a regular playlist
    fun createPlaylist(name: String, description: String) {
        val newPlaylist = Playlist(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            songs = emptyList(),
            coverImageResId = R.drawable.song6new // You can set a default image
        )
        _playlists.value = _playlists.value + newPlaylist
    }

    // Function to add a song to a specific playlist
    fun addSongToPlaylist(playlistId: String, song: RawSong) {
        // Don't allow manual addition to liked songs playlist
        if (playlistId == "liked_songs_playlist") {
            return
        }

        _playlists.value = _playlists.value.map { playlist ->
            if (playlist.id == playlistId && !playlist.songs.contains(song)) {
                playlist.copy(songs = playlist.songs + song)
            } else {
                playlist
            }
        }
    }

    // Function to remove a song from a playlist
    fun removeSongFromPlaylist(playlistId: String, song: RawSong) {
        _playlists.value = _playlists.value.map { playlist ->
            if (playlist.id == playlistId) {
                playlist.copy(songs = playlist.songs.filter { it.id != song.id })
            } else {
                playlist
            }
        }
    }

    // Function to delete a playlist (except liked songs)
    fun deletePlaylist(playlistId: String) {
        if (playlistId == "liked_songs_playlist") {
            return // Don't allow deletion of liked songs playlist
        }

        _playlists.value = _playlists.value.filter { it.id != playlistId }
    }

    // Selected playlist for navigation
    private val _selectedPlaylist = MutableStateFlow<Playlist?>(null)
    val selectedPlaylist: StateFlow<Playlist?> = _selectedPlaylist

    fun selectPlaylist(playlist: Playlist) {
        _selectedPlaylist.value = playlist
    }

    // Get liked songs as a list
    fun getLikedSongsList(): List<RawSong> {
        val likedSongIds = _likedSongs.value
        return _rawSongs.filter { song -> likedSongIds.contains(song.id) }
    }

    // Check if a song is liked
    fun isSongLiked(songId: String): Boolean {
        return _likedSongs.value.contains(songId)
    }



}

