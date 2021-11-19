package com.sergio.monthlyne.entity

data class PostInformation(
    var id:String = "",
    var userId:String = "",
    var postDate:String = "",
    var postContent:String = "",
    var postScore:Int = 0,
    var postName:String = "",
    var postPhotoURL:String = "",
    var postLikeCounter:MutableList<String> = mutableListOf(),
    var postDislikeCounter:MutableList<String> = mutableListOf()

){
    fun userVoted (userId : String): Int {
        return when {
            postLikeCounter.contains(userId) -> {
                1
            }
            postDislikeCounter.contains(userId) -> {
                2
            }
            else -> {
                0
            }
        }
    }
}
