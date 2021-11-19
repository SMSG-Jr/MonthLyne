package com.sergio.monthlyne.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.RankedPostInfo

class RankedPostAdapter : RecyclerView.Adapter<RankedPostAdapter.RankedViewHolder>() {
    private var rankedPostList : List<RankedPostInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranked_post, parent, false)
        return RankedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankedViewHolder, position: Int) {
        holder.set(rankedPostList[position])
    }

    override fun getItemCount(): Int {
        return rankedPostList.size
    }

    fun update(rankedList: List<RankedPostInfo>) {
        this.rankedPostList = rankedList
        notifyDataSetChanged()
    }

    class RankedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val userPhoto : RoundedImageView = itemView.findViewById(R.id.ranked_post_user_photo)
        private val userName : TextView = itemView.findViewById(R.id.ranked_post_user_name)
        private val postDate : TextView = itemView.findViewById(R.id.ranked_post_date)
        private val postMessage : TextView = itemView.findViewById(R.id.ranked_post_message)
        private val postLike : TextView = itemView.findViewById(R.id.text_ranked_post_like)
        private val postDislike : TextView = itemView.findViewById(R.id.text_ranked_post_dislike)
        private val postRank : TextView = itemView.findViewById(R.id.text_post_rank)

        fun set(rankedPost: RankedPostInfo) {
            postDate.text = rankedPost.rankedPostDate
            postMessage.text = rankedPost.rankedPostContent
            postLike.text = rankedPost.rankedPostLike
            postDislike.text = rankedPost.rankedPostDislike
            userName.text = rankedPost.rankedPostName
            postRank.text = rankedPost.rankedPostRank
            Glide.with(itemView.context).load(rankedPost.rankedPostPhotoURL).into(userPhoto)
        }
    }

}