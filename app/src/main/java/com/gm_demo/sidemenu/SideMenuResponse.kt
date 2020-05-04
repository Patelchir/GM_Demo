package com.gm_demo.sidemenu

data class SideMenuResponse(
    val genres: List<Genre>
) {
    data class Genre(
        val id: String,
        val name: String,
        var isActive: Boolean = false
    )
}