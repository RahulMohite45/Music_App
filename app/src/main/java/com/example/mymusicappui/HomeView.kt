package com.example.mymusicappui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Data classes
data class UserProfile(
    val name: String,
    val localImageRes: Int? = null
)

data class MusicCard(
    val title: String,
    val artist: String? = null,
    val imageRes: Int? = null
)

data class AlbumCard(
    val title: String,
    val artist: String,
    val imageRes: Int? = null
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(){
    val purpleGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8B5CF6),
            Color(0xFF6366F1),
            Color(0xFF3B82F6)
        )
    )

    val darkBackground = Color(0xFF1A1A2E)
    val cardBackground = Color(0xFF16213E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Discover",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = darkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.3f),
                            darkBackground
                        )
                    )
                )
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // User Profiles Section
            item {
                UserProfilesSection()
            }

            // Search Bar
            item {
                SearchBar()
            }

            // Made for You Section
            item {
                MadeForYouSection()
            }

            // Discover Weekly Section
            item {
                DiscoverWeeklySection()
            }

            // Today Hits Section
            item {
                TodayHitsSection()
            }
        }
    }
}

@Composable
fun UserProfilesSection() {
    val profiles = listOf(
        UserProfile("John",R.drawable.johnsinger11),
        UserProfile("Emma",R.drawable.emmasinger11),
        UserProfile("Mike",R.drawable.mikesinger11),
        UserProfile("Sarah",R.drawable.sarahsinger11)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(profiles) { profile ->
                UserProfileCard(profile)
            }
        }
    }
}

@Composable
fun UserProfileCard(profile: UserProfile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        if (profile.localImageRes != null) {
            Image(
                painter = painterResource(id = profile.localImageRes),
                contentDescription = "Profile Image of ${profile.name}",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6),
                                Color(0xFF6366F1)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = profile.name,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = {
            Text(
                "Search music",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFF16213E),
            focusedBorderColor = Color(0xFF8B5CF6),
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun MadeForYouSection() {
    val madeForYouItems = listOf(
        MusicCard("Whistle", "Flo Ride",R.drawable.whistlepage),
        MusicCard("Tu Abhaal", "Javed Ali ",R.drawable.tuabhaalcover)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Made for you",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(madeForYouItems) { item ->
                MusicItemCard(item)
            }
        }
    }
}

@Composable
fun MusicItemCard(musicCard: MusicCard) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(180.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            if(musicCard.imageRes != null){
                Image(
                    painter = painterResource(id = musicCard.imageRes),
                    contentDescription = "Album cover for ${musicCard.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6),
                                    Color(0xFF6366F1)
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = musicCard.title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            musicCard.artist?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun DiscoverWeeklySection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B5CF6)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Discover Weekly",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Your weekly mix",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for user image
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1))
                )
            }
        }
    }
}

@Composable
fun TodayHitsSection() {
    val todayHits = listOf(
        AlbumCard("Hit 1", "Artist 1"),
        AlbumCard("Hit 2", "Artist 2"),
        AlbumCard("Hit 3", "Artist 3"),
        AlbumCard("Hit 4", "Artist 4")
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today hits",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See all",
                color = Color(0xFF8B5CF6),
                fontSize = 14.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(todayHits) { hit ->
                AlbumItemCard(hit)
            }
        }
    }
}

@Composable
fun AlbumItemCard(album: AlbumCard) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B6B),
                                Color(0xFF4ECDC4),
                                Color(0xFF45B7D1),
                                Color(0xFFFF9500)
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = album.title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 1
            )

            Text(
                text = album.artist,
                color = Color.Gray,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}

