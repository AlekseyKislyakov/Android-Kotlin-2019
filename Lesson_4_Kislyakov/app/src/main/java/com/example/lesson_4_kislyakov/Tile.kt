package com.example.lesson_4_kislyakov

open class Tile(val header: String, val description: String? = null, val avatar: Int, var red: Boolean = false)

class TileDetail(header: String, description: String?, avatar: Int, red: Boolean) :
    Tile(header, description, avatar, red)

class TileBase(header: String, description: String?, avatar: Int, red: Boolean) :
    Tile(header, description, avatar, red)
