package com.example.facebookappclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facebookappclone.R
import com.example.facebookappclone.activity.MainActivity
import com.example.facebookappclone.model.Feed
import com.example.facebookappclone.model.LinkPost
import com.google.android.material.imageview.ShapeableImageView

class FeedAdapter(var context: MainActivity, var items: ArrayList<Feed>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM_HEAD = 0
        private const val TYPE_ITEM_STORY = 1
        private const val TYPE_ITEM_POST = 2
        private const val TYPE_ITEM_LINK_POST = 3
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addFeed(feed: Feed) {
        items.add(2, feed)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val feed = items[position]
        return when {
            feed.isHeader -> TYPE_ITEM_HEAD
            feed.stories.size > 0 -> TYPE_ITEM_STORY
            feed.linkPost != null -> TYPE_ITEM_LINK_POST
            else -> TYPE_ITEM_POST
        }
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

            if (holder.adapter == null) {
                holder.adapter = StoryAdapter(context, stories)
                recyclerView.adapter = holder.adapter
            }
        }

        if (holder is PostViewHolder) {
            Glide.with(context).load(feed.post!!.profile).centerCrop().into(holder.ivProfile)
            Glide.with(context).load(feed.post!!.post).fitCenter().into(holder.ivPost)
            holder.tvFullName.text = feed.post!!.fullName
        }

        if (holder is HeadViewHolder) {
            Glide.with(context).load(feed.profile).centerCrop().into(holder.ivProfile)
            holder.tvMind.setOnClickListener {
                val linkPost = LinkPost(feed.profile, feed.fullName)
                context.callObjectTransfer(linkPost)
            }
        }

        if (holder is LinkPostViewHolder) {
            val linkPost = feed.linkPost!!

            Glide.with(context).load(linkPost.profile).centerCrop().into(holder.ivProfile)
            holder.tvFullName.text = linkPost.fullName
            holder.tvPost.text = findUrl(linkPost.post.toString(), linkPost.link.toString())
            linkPost.image.let { Glide.with(context).load(it).fitCenter().into(holder.ivPost) }

            if (linkPost.title == null || linkPost.description == null)
                holder.llAbout.visibility = View.GONE

            holder.tvTitle.text = linkPost.title
            holder.tvDescription.text = linkPost.description

        }
    }

    private fun findUrl(text: String, url: String): Spannable {
        val spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#0071FA")),
            text.indexOf(url),
            text.indexOf(url) + url.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
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
        var adapter: StoryAdapter? = null

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
        val llAbout: LinearLayout = view.findViewById(R.id.ll_about)
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val tvFullName: TextView = view.findViewById(R.id.tv_fullName)
        val ivPost: ImageView = view.findViewById(R.id.iv_post)
        val tvPost: TextView = view.findViewById(R.id.tv_post)
        val tvTitle: TextView = view.findViewById(R.id.tv_address)
        val tvDescription: TextView = view.findViewById(R.id.tv_title)
    }
}