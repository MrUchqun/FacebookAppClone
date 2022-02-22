package com.example.facebookappclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookappclone.R
import com.example.facebookappclone.adapter.FeedAdapter
import com.example.facebookappclone.model.Feed
import com.example.facebookappclone.model.Post
import com.example.facebookappclone.model.Story

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        const val profile1 =
            "https://images.unsplash.com/photo-1589624311490-a69ce6432820?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
        const val profile2 =
            "https://images.unsplash.com/photo-1564564321837-a57b7070ac4f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1176&q=80"
        const val profile3 =
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=461&q=80"
        const val profile4 =
            "https://avatars.githubusercontent.com/u/10227536?v=4"

        const val photo1 =
            "https://images.unsplash.com/photo-1644945583242-1f7fdb6087ea?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
        const val photo2 =
            "https://images.unsplash.com/photo-1640622304293-ec9c89c6bac9?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"
        const val photo3 =
            "https://images.unsplash.com/photo-1486286701208-1d58e9338013?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
        const val photo4 =
            "https://images.unsplash.com/photo-1644952720775-c769200e6b67?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"
        const val photo5 =
            "https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1025&q=80"

        const val video1 =
            "https://vod-progressive.akamaized.net/exp=1645096363~acl=%2Fvimeo-prod-skyfire-std-us%2F01%2F3400%2F21%2F542004485%2F2570352312.mp4~hmac=6726a3a94819327cf3134630bce9f0858506aca5403440abad56d5f1cb4f0573/vimeo-prod-skyfire-std-us/01/3400/21/542004485/2570352312.mp4?filename=pexels-mart-production-7667423.mp4"
        const val video2 =
            "https://vod-progressive.akamaized.net/exp=1645097242~acl=%2Fvimeo-prod-skyfire-std-us%2F01%2F2251%2F16%2F411256287%2F1766006419.mp4~hmac=859496a5d3b09cd410a0e08890353daddfb0586c0ff02b790d3951d1a783d464/vimeo-prod-skyfire-std-us/01/2251/16/411256287/1766006419.mp4?filename=production+ID%3A4228659.mp4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        refreshAdapter(getAllFeeds())
    }

    private fun refreshAdapter(allFeeds: ArrayList<Feed>) {
        recyclerView.adapter = FeedAdapter(this, allFeeds)
    }

    private fun getAllFeeds(): ArrayList<Feed> {
        val stories = ArrayList<Story>()
        for (i in 0..3) {
            stories.add(Story(profile1, "Bekzod Niyozov", photo1))
            stories.add(Story(profile2, "Surojbek Rozzoqov", photo2))
            stories.add(Story(profile3, "Asila Temirova", photo3))
            stories.add(Story(profile4, "Xurshidbek Kurbanov", photo4))
        }

        val feeds = ArrayList<Feed>()
        //add Head
        feeds.add(Feed(profile1))
        //add Story
        feeds.add(Feed(stories))
        //add Posts
        for (i in 0..3) {
            feeds.add(Feed(Post(profile1, "Bekzod Niyozov", photo1)))
            feeds.add(Feed(Post(profile2, "Surojbek Rozzoqov", photo2)))
            feeds.add(Feed(Post(profile3, "Asila Temirova", photo3)))
            feeds.add(Feed(Post(profile4, "Xurshidbek Kurbanov", photo4)))
        }

        return feeds
    }
}