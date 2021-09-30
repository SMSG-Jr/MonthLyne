package com.sergio.monthlyne.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.sergio.monthlyne.R
import com.sergio.monthlyne.activity.FeedActivity
import com.sergio.monthlyne.entity.PostInformation

class PostAdapter() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var postList : List<PostInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.set(postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun update(postList: List<PostInformation>) {
        this.postList = postList
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val userPhoto : RoundedImageView = itemView.findViewById(R.id.post_user_photo)
        private val userName : TextView = itemView.findViewById(R.id.post_user_name)
        private val postDate : TextView = itemView.findViewById(R.id.post_date)
        private val postMessage : TextView = itemView.findViewById(R.id.post_message)
        private val postScore : TextView = itemView.findViewById(R.id.post_score)

        fun set(post : PostInformation){
            postDate.text = post.postDate
            postMessage.text = post.postContent
            postScore.text = post.postScore.toString()
            userName.text = post.postName
            Glide.with(itemView.context).load(post.postPhotoURL).into(userPhoto)
        }
    }
}