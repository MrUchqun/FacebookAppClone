package com.example.facebookappclone.model

import java.io.Serializable

data class LinkPost(var profile: String? = null, var fullName: String? = null) : Serializable {

    var post: String? = null
    var link: String? = null
    var image: String? = null
    var title: String? = null
    var description: String? = null
}