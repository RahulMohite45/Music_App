package com.example.mymusicappui.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mymusicappui.R
import com.example.mymusicappui.RawSong

@Composable
fun SearchScreen(rawSongs: List<RawSong>){
    var query by remember { mutableStateOf("") }



    val filteredSongs = rawSongs.filter {
        it.title.contains(query, ignoreCase = true)
        it.artist.contains(query, ignoreCase = true)
        it.duration.contains(query, ignoreCase = true)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ){
        OutlinedTextField(
            value = query,
            onValueChange = {query = it},
            placeholder = {Text("Search Playlists & Favourite songs")},
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null)},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        //TODO show filtered songs based on query
       LazyColumn {
           items(filteredSongs){song->
               Text(
                   text = "${song.title}-${song.artist}-${song.duration}",
                   style = MaterialTheme.typography.bodyLarge,
                   color = Color.Black,
                   modifier = Modifier.padding(vertical = 4.dp)

               )
           }
       }
    }
}

