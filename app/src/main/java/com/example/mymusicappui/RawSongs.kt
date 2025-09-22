package com.example.mymusicappui
import java.util.UUID
data class RawSong(

    val title: String,
    val artist: String,
    val duration: String,
    val resId: Int,
    val imageResId: Int,
    val rawResId: Int,
    val id:String = UUID.randomUUID().toString(),
    val thumb: String? = null,


)
val rawSongs = listOf(
    RawSong("Uyi amma ","Amit Trivedi","2:45", R.raw.song1,R.drawable.song1new,R.raw.song11111new, thumb = ""),
    RawSong("Sad Song", "Sapne Bade","4:10",R.raw.song2,R.drawable.song2,R.raw.song2, thumb = ""),
    RawSong("lofi Song","lofium","3:04",R.raw.song3,R.drawable.song3,R.raw.song3, thumb = ""),
    RawSong("Bade Ache Lagta ahe","Amit Kumar","3:48",R.raw.song4,R.drawable.song4,R.raw.song4, thumb = ""),
    RawSong("Pal Pal dil ke pass","Kishor kumar","5:26",R.raw.song5,R.drawable.song5,R.raw.song5, thumb = ""),
    RawSong("Ye tuna kay kiya","Pritam,Javed","5:04",R.raw.song6,R.drawable.song6new,R.raw.song6)
)

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val songs: List<RawSong>,
    val coverImageResId: Int
)