package com.example.facebookappclone.model

class Post(
    var profile: String,
    var fullName: String,
    var post: String? = null,
    var isLinkPost: Boolean = false
) {
}