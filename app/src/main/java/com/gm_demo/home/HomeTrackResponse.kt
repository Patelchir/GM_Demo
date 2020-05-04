package com.gm_demo.home

data class HomeTrackResponse(
    val tracks: List<Track>
) {
    data class Track(
        val albumId: String,
        val albumName: String,
        val amg: String,
        val artistId: String,
        val artistName: String,
        val blurbs: List<Any>,
        val disc: Int,
        val href: String,
        val id: String,
        val index: Int,
        val isAvailableInHiRes: Boolean,
        val isExplicit: Boolean,
        val isStreamable: Boolean,
        val isrc: String,
        val name: String,
        val playbackSeconds: Int,
        val previewURL: String,
        val shortcut: String,
        val type: String,
        var isPlaying: Boolean = false
    )
}