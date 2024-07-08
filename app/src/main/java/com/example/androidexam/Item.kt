package com.example.androidexam

data class Item(
    val text: String,
    val subText: String,
    val imageUrl: String?, // URL for the image (could be null if it's a local resource)
    val imageResourceId: Int?, // Local image resource ID (could be null if it's a URL)

) {
    constructor(text: String,subText: String, imageUrl: String) : this(text, subText, imageUrl, 0) {}
    constructor(text: String,subText: String,imageResourceId: Int) : this(text,subText, "", imageResourceId) {}
}