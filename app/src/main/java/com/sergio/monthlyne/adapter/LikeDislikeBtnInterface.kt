package com.sergio.monthlyne.adapter

import com.sergio.monthlyne.entity.PostInformation

interface LikeDislikeBtnInterface {
    fun likeButtonConfig(postInfo:PostInformation, position:Int)
    fun dislikeButtonConfig(postInfo:PostInformation, position:Int)
}