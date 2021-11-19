package com.sergio.monthlyne.entity

data class RankedPostInfo(
    var rankedPostUserId : String = "",
    var rankedPostRank : String = "1",
    var rankedPostPhotoURL : String = "",
    var rankedPostName : String = "",
    var rankedPostDate : String = "",
    var rankedPostContent : String = "",
    var rankedPostLike : String = "0",
    var rankedPostDislike : String = "0"
)
