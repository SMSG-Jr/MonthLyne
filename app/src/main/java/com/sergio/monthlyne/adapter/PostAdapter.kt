package com.sergio.monthlyne.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.PostInformation

class PostAdapter(private val likeDislikeBtnInterface: LikeDislikeBtnInterface) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postList : List<PostInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.set(postList[position])
        holder.postLikeButton.setOnClickListener {
            likeDislikeBtnInterface.likeButtonConfig(postList[position], position)
        }
        holder.postDislikeButton.setOnClickListener {
            likeDislikeBtnInterface.dislikeButtonConfig(postList[position], position)
        }
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
        private val postMessage : TextView = itemView.findViewById(R.id.post_profile_message)
        private val postLike : TextView = itemView.findViewById(R.id.post_score_like)
        private val postDislike : TextView = itemView.findViewById(R.id.post_score_dislike)
        val postLikeButton : ImageButton = itemView.findViewById(R.id.button_thumb_up)
        val postDislikeButton : ImageButton = itemView.findViewById(R.id.button_thumb_down)

        fun set(post : PostInformation){
            postDate.text = post.postDate
            postMessage.text = post.postContent
            postLike.text = post.postLikeCounter.size.toString()
            postDislike.text = post.postDislikeCounter.size.toString()
            userName.text = post.postName
            Glide.with(itemView.context).load(post.postPhotoURL).into(userPhoto)
        }
    }
}