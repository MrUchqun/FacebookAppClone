package com.example.facebookappclone.adapter

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facebookappclone.R
import com.example.facebookappclone.activity.CreatePostActivity
import com.example.facebookappclone.model.Feed
import com.example.facebookappclone.model.Story
import com.google.android.material.imageview.ShapeableImageView

class FeedAdapter(var context: Context, var items: ArrayList<Feed>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM_HEAD = 0
        private const val TYPE_ITEM_STORY = 1
        private const val TYPE_ITEM_POST = 2
        private const val TYPE_ITEM_LINK_POST = 3
    }

    private var urlAddress: String? = null

    override fun getItemViewType(position: Int): Int {
        val feed = items[position]
        return when {
            feed.isHeader -> TYPE_ITEM_HEAD
            feed.stories.size > 0 -> TYPE_ITEM_STORY
            feed.post!!.isLinkPost -> TYPE_ITEM_LINK_POST
            else -> TYPE_ITEM_POST
        }
    }

    private fun containsLink(input: String): Boolean {
        var result = false
        val parts = input.split("\\s+").toTypedArray()
        for (item in parts) {
            if (Patterns.WEB_URL.matcher(item).matches()) {
                result = true
                urlAddress = item
                break
            }
        }
        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_ITEM_HEAD -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_feed_head, parent, false)
                HeadViewHolder(context, view)
            }
            TYPE_ITEM_STORY -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_feed_story, parent, false)
                StoryViewHolder(context, view)
            }
            TYPE_ITEM_LINK_POST -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_feed_link_post, parent, false)
                LinkPostViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_feed_post, parent, false)
                PostViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feed = items[position]

        if (holder is StoryViewHolder) {
            val recyclerView = holder.recyclerView
            val stories = feed.stories
            refreshAdapter(recyclerView, stories)
        }

        if (holder is PostViewHolder) {
            Glide.with(context).load(feed.post!!.profile).centerCrop().into(holder.ivProfile)
            Glide.with(context).load(feed.post!!.post).fitCenter().into(holder.ivPost)
            holder.tvFullName.text = feed.post!!.fullName
        }

        if (holder is HeadViewHolder) {
            Glide.with(context).load(feed.profile).centerCrop().into(holder.ivProfile)
            holder.tvMind.setOnClickListener {
                context.startActivity(Intent(context, CreatePostActivity::class.java))
            }
        }

        if (holder is LinkPostViewHolder) {
            Glide.with(context).load(feed.post!!.profile).centerCrop().into(holder.ivProfile)
            holder.tvFullName.text = feed.post!!.fullName


        }
    }

    private fun refreshAdapter(recyclerView: RecyclerView, stories: ArrayList<Story>) {
        val adapter = StoryAdapter(context, stories)
        recyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val tvFullName: TextView = view.findViewById(R.id.tv_fullName)
        val ivPost: ShapeableImageView = view.findViewById(R.id.iv_post)
    }

    class StoryViewHolder(context: Context, view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        init {
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    class HeadViewHolder(context: Context, view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val tvMind: TextView = view.findViewById(R.id.tv_new_mind)
    }

    class LinkPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val tvFullName: TextView = view.findViewById(R.id.tv_fullName)
        val ivPost: ImageView = view.findViewById(R.id.iv_post)
        val tvPost: TextView = view.findViewById(R.id.tv_post)
        val tvAddress: TextView = view.findViewById(R.id.tv_address)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
    }
}