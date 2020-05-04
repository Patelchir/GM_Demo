package com.gm_demo.sidemenu

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SideMenu(
    var position: String,
    var title: String,
    val icon: String,
    var isActive: Boolean
)

