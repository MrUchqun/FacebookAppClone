package com.example.facebookappclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookappclone.R
import com.example.facebookappclone.adapter.FeedAdapter
import com.example.facebookappclone.helper.Data
import com.example.facebookappclone.helper.Data.photo1
import com.example.facebookappclone.helper.Data.photo2
import com.example.facebookappclone.helper.Data.photo3
import com.example.facebookappclone.helper.Data.photo4
import com.example.facebookappclone.helper.Data.profile1
import com.example.facebookappclone.helper.Data.profile2
import com.example.facebookappclone.helper.Data.profile3
import com.example.facebookappclone.helper.Data.profile4
import com.example.facebookappclone.model.Feed
import com.example.facebookappclone.model.LinkPost
import com.example.facebookappclone.model.Post
import com.example.facebookappclone.model.Story
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeedAdapter

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
        adapter = FeedAdapter(this, allFeeds)
        recyclerView.adapter = adapter
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
        feeds.add(Feed(profile1, "Bekzod Niyozov"))
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

    fun callObjectTransfer(linkPost: LinkPost) {
        val intent = Intent(this, CreatePostActivity::class.java)
        intent.putExtra("linkPost", linkPost)
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val linkPost = data.getSerializableExtra("newPost") as LinkPost
                    adapter.addFeed(Feed(linkPost))
                }
            }
        }
    }

    var count = 0
    override fun onBackPressed() {
        count++
        if (count < 2) {
            Toast.makeText(this, "Back button press, please!", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}