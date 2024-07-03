package com.example.androidexam

data class Item(
    val text: String,
    val imageUrl: String?, // URL for the image (could be null if it's a local resource)
    val imageResourceId: Int?, // Local image resource ID (could be null if it's a URL)

) {
    constructor(text: String, imageUrl: String) : this(text, imageUrl, 0) {}
    constructor(text: String, imageResourceId: Int) : this(text, "", imageResourceId) {}
}