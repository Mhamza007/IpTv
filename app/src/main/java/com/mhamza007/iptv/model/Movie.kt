package com.mhamza007.iptv.model

data class Movie(val icon: String, val name: String, val rating: String) {
    constructor() : this("", "", "")
}