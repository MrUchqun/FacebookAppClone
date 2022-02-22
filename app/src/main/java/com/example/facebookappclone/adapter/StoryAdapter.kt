package com.example.facebookappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facebookappclone.R
import com.example.facebookappclone.model.Story

class StoryAdapter(var context: Context, var items: ArrayList<Story>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_VIEW_HEADER = 0
        private const val TYPE_VIEW_STORY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_VIEW_HEADER else TYPE_VIEW_STORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_VIEW_HEADER) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_story_view, parent, false)
            return StoryViewHolder(view)
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_view, parent, false)
        return StoryViewHolder(view)
    }

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val tvFullName: TextView = view.findViewById(R.id.tv_fullName)
        val ivBackground: ImageView = view.findViewById(R.id.iv_background)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val story = items[position]
        if (holder is StoryViewHolder) {
            if (position != 0) {
                holder.tvFullName.text = story.fullName
                Glide.with(context).load(story.story).centerCrop()
                    .into(holder.ivBackground)
            }

            Glide.with(context).load(story.profile).centerCrop().into(holder.ivProfile)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}