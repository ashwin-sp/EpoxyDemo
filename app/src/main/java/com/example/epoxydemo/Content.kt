package com.example.epoxydemo

data class Content(val id: Int, var by: String, var content: String = "", var likeCount: Int = 0, val feedID: Int, val commentID: Int)