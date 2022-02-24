package com.example.facebookappclone.model

class Feed {

    var isHeader: Boolean = false
    var post: Post? = null
    var stories: ArrayList<Story> = ArrayList()
    var linkPost: LinkPost? = null

    var profile: String? = null
    var fullName: String? = null

    constructor(profile: String, fullName: String) {
        this.profile = profile
        this.fullName = fullName
        this.isHeader = true
    }

    constructor(post: Post) {
        this.post = post
        this.isHeader = false
    }

    constructor(stories: ArrayList<Story>) {
        this.stories = stories
        this.isHeader = false
    }

    constructor(linkPost: LinkPost) {
        this.linkPost = linkPost
        isHeader = false
    }
}