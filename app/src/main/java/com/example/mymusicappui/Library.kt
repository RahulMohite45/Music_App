package com.example.mymusicappui


// Core Android and Kotlin imports
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

// Compose imports
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// ViewModel imports
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Library(viewModel: MainViewModel = viewModel()){

    val rawSongs = viewModel.rawSongs
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1B2F),
                        Color(0xFF3C3B54))
    )
    val playlists by viewModel.playlists.collectAsState()// or however you're managing playlists in your viewModel
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var showAddToPlaylistDialog by remember { mutableStateOf(false) }
    var selectedSongForPlaylist by remember { mutableStateOf<RawSong?>(null) }

    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_AUDIO
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionGranted = remember{ mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted){
            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            permissionGranted.value = true
            //TODO fetch songs
            viewModel.loadSongs(context)
        }else{
            Toast.makeText(context,"Permission is required to access songs",Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        } else {
            permissionGranted.value = true
            //TODO FETCH SONGS
            viewModel.loadSongs(context)
        }
    }
    // To setup the top small boxes
    var selectedFilter by remember{ mutableStateOf("Playlists") }
    val filters = listOf("Playlist","Podcast","Albums","Artists")
    var isGridView by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter
                Button(
                    onClick = { selectedFilter = filter },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.White else Color.DarkGray,
                        contentColor = if (isSelected) Color.Black else Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(text = filter)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            if (selectedFilter == "Playlists"){
                Button(
                    onClick = { showCreatePlaylistDialog = true},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Create Playlist",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create Playlist", color = Color.White)
                }
            }

            IconButton(onClick = {isGridView = !isGridView},
                modifier = Modifier.padding(0.dp)
            ) {
                val iconRes = if(isGridView) R.drawable.list4 else R.drawable.squaregride1
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Toggle View",
                    tint = Color.White
                )
            }
        }
        if (permissionGranted.value) {
            when (selectedFilter) {
                "Playlists" -> {
                    if (isGridView) {
                        PlaylistGridView(playlists = playlists, viewModel = viewModel)
                    } else {
                        PlaylistListView(playlists = playlists, viewModel = viewModel)
                    }
                }
                "Songs" -> {
                    if (isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(rawSongs) { song ->
                                RawSongItem(
                                    song = song,
                                    isGrid = true,
                                    onAddToPlaylist = {
                                        selectedSongForPlaylist = song
                                        showAddToPlaylistDialog = true
                                    }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(rawSongs) { song ->
                                RawSongItem(
                                    song = song,
                                    isGrid = false,
                                    onAddToPlaylist = {
                                        selectedSongForPlaylist = song
                                        showAddToPlaylistDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                // Add other filter implementations here
            }
        }
    }

    // Create Playlist Dialog
    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            onDismiss = { showCreatePlaylistDialog = false },
            onConfirm = { name, description ->
                viewModel.createPlaylist(name, description)
                showCreatePlaylistDialog = false
            }
        )
    }

    // Add to Playlist Dialog
    if (showAddToPlaylistDialog && selectedSongForPlaylist != null) {
        AddToPlaylistDialog(
            playlists = playlists,
            onDismiss = { showAddToPlaylistDialog = false },
            onAddToPlaylist = { playlist ->
                viewModel.addSongToPlaylist(playlist.id, selectedSongForPlaylist!!)
                showAddToPlaylistDialog = false
                selectedSongForPlaylist = null
                Toast.makeText(context, "Song added to ${playlist.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// 4. Update RawSongItem to include playlist functionality
@Composable
fun RawSongItem(
    song: RawSong,
    isGrid: Boolean,
    onAddToPlaylist: () -> Unit = {}
) {
    if (isGrid) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Image(
                    painter = painterResource(id = song.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                )
                IconButton(
                    onClick = onAddToPlaylist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            CircleShape
                        )
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add), // Add icon
                        contentDescription = "Add to Playlist",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = song.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = song.artist,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            Text(
                text = song.duration,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = song.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = song.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Artist: ${song.artist}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Duration: ${song.duration}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onAddToPlaylist) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add to Playlist",
                    tint = Color.White
                )
            }
        }
    }
}

// 5. Playlist Views
@Composable
fun PlaylistGridView(playlists: List<Playlist>, viewModel: MainViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist = playlist, isGrid = true, viewModel = viewModel)
        }
    }
}

@Composable
fun PlaylistListView(playlists: List<Playlist>, viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist = playlist, isGrid = false, viewModel = viewModel)
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, isGrid: Boolean, viewModel: MainViewModel) {
    if (isGrid) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { viewModel.selectPlaylist(playlist) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = playlist.coverImageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )
            Text(
                text = playlist.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = "${playlist.songs.size} songs",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { viewModel.selectPlaylist(playlist) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = playlist.coverImageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = playlist.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${playlist.songs.size} songs",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// 6. Dialog Components
@Composable
fun CreatePlaylistDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    var playlistDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Playlist") },
        text = {
            Column {
                TextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("Playlist Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = playlistDescription,
                    onValueChange = { playlistDescription = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (playlistName.isNotBlank()) {
                        onConfirm(playlistName, playlistDescription)
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddToPlaylistDialog(
    playlists: List<Playlist>,
    onDismiss: () -> Unit,
    onAddToPlaylist: (Playlist) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Playlist") },
        text = {
            LazyColumn {
                items(playlists) { playlist ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddToPlaylist(playlist) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_playlist),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = playlist.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "${playlist.songs.size} songs",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}