package com.sergio.monthlyne.entity

data class PostInformation(
    var id:String = "",
    var userId:String = "",
    var postDate:String = "",
    var postContent:String = "",
    var postScore:Int = 0,
    var postName:String = "",
    var postPhotoURL:String = ""
)
