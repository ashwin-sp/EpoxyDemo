package com.example.epoxydemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    var batsmen: ArrayList<String> = ArrayList(listOf("Ashwin", "Annan", "Muthu", "Bhai"))
    var allrounders: ArrayList<String> = ArrayList(listOf("Mahesh", "Bairavi"))
    var bowlers: ArrayList<String> = ArrayList(listOf("Assault", "Suruli", "Sanjeevi", "Subhankar", "Lakshmi"))

    var contentList: ArrayList<Content> = ArrayList()
    var commentID = -1
    var feedID = -1
    var nextId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //  val controller = ContentController()
        recycler_view.layoutManager = LinearLayoutManager(this)
        //   recycler_view.setController(controller)
        //  controller.setData(listOf("1", "2", "3"), true, this)
        //  controller.requestModelBuild()
        prefill()
        updateModels()
        addContentListener()
        showSoftKeyboard(edit_content)
    }

    private fun addContentListener() {
        add.setOnClickListener {
            if (edit_content.text.toString().trim().isNotEmpty()) {
                when {
                    commentID != -1 -> {
                        contentList.add(Content(nextId, "A", edit_content.text.toString().trim(), 0, feedID, commentID))
                        nextId++
                        recycler_view.requestModelBuild()
                    }
                    feedID != -1 -> {
                        contentList.add(Content(nextId, "A", edit_content.text.toString().trim(), 0, feedID, -1))
                        nextId++
                        recycler_view.requestModelBuild()
                    }
                    else -> {
                        contentList.add(Content(nextId, "A", edit_content.text.toString().trim(), 0, -1, -1))
                        nextId++
                        recycler_view.requestModelBuild()
                    }
                }
                recycler_view.smoothScrollToPosition(nextId - 2)
                hideSoftKeyboard(edit_content)
            }
        }
    }

    private fun prefill() {
        contentList.add(Content(1, "A", "Hello world", 0, -1, -1))
        contentList.add(Content(2, "S", "Whats up???", 2, -1, -1))
        contentList.add(Content(3, "K", "Yo dude, we are being hacked daily!", 1, -1, -1))
        contentList.add(Content(4, "S", "Hey", 1, 1, -1))
        contentList.add(Content(5, "A", "Good morning", 1, 1, 4))
        contentList.add(Content(6, "S", "Have a good day!", 2, 1, 4))
        contentList.add(Content(7, "A", "Its new year!", 2, 2, -1))
        nextId = 8
    }


    fun updateModels() {
        recycler_view.buildModelsWith { controller ->
            run {
                when {
                    commentID != -1 -> {
                        contentList.forEach {
                            if (it.id == commentID) {
                                val commentContent = it
                                CommentItemBindingModel_().id(it.id).content(it).onLiked(View.OnClickListener {
                                    contentList[commentContent.id - 1] = Content(commentContent.id, commentContent.by, commentContent.content, contentList[commentContent.id - 1].likeCount + 1, commentContent.feedID, commentContent.commentID)
                                    recycler_view.requestModelBuild()
                                }).listener(View.OnClickListener {
                                    showSoftKeyboard(edit_content)
                                }).addTo(controller)
                                contentList.forEach {
                                    if (commentID == it.commentID) {
                                        val replyCommentContent = it
                                        ReplyCommentItemBindingModel_().id(it.id).content(it).onLiked(View.OnClickListener {
                                            contentList[replyCommentContent.id - 1] = Content(replyCommentContent.id, replyCommentContent.by, replyCommentContent.content, contentList[replyCommentContent.id - 1].likeCount + 1, replyCommentContent.feedID, replyCommentContent.commentID)
                                            recycler_view.requestModelBuild()
                                        }).listener(View.OnClickListener {
                                            showSoftKeyboard(edit_content)
                                        }).addTo(controller)
                                    }
                                }
                            }
                        }

                    }
                    feedID != -1 -> {
                        contentList.forEach {
                            if (it.id == feedID) {
                                val content = it
                                FeedItemBindingModel_().id(it.id).content(it).onLiked(View.OnClickListener {
                                    contentList[content.id - 1] = Content(content.id, content.by, content.content, contentList[content.id - 1].likeCount + 1, content.feedID, content.commentID)
                                    recycler_view.requestModelBuild()
                                }).listener(View.OnClickListener {
                                    feedID = content.id
                                    recycler_view.requestModelBuild()
                                    showSoftKeyboard(edit_content)
                                }).addTo(controller)
                            }
                            if (it.feedID == feedID && it.commentID == -1) {
                                val currentCommentId = it.id
                                val commentContent = it
                                CommentItemBindingModel_().id(currentCommentId).content(it).listener(View.OnClickListener {
                                    commentID = currentCommentId
                                    recycler_view.requestModelBuild()
                                    showSoftKeyboard(edit_content)
                                }).onLiked(View.OnClickListener {
                                    contentList[commentContent.id - 1] = Content(commentContent.id, commentContent.by, commentContent.content, contentList[commentContent.id - 1].likeCount + 1, commentContent.feedID, commentContent.commentID)
                                    recycler_view.requestModelBuild()
                                }).addTo(controller)
                                contentList.forEach {
                                    if (it.commentID == currentCommentId) {
                                        val replyCommentContent = it
                                        ReplyCommentItemBindingModel_().id(it.id).content(it).onLiked(View.OnClickListener {
                                            contentList[replyCommentContent.id - 1] = Content(replyCommentContent.id, replyCommentContent.by, replyCommentContent.content, contentList[replyCommentContent.id - 1].likeCount + 1, replyCommentContent.feedID, replyCommentContent.commentID)
                                            recycler_view.requestModelBuild()
                                        }).listener(View.OnClickListener {
                                            commentID = currentCommentId
                                            recycler_view.requestModelBuild()
                                            showSoftKeyboard(edit_content)
                                        }).addTo(controller)
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        contentList.forEach {
                            if (it.commentID == -1 && it.feedID == -1) {
                                val content = it
                                FeedItemBindingModel_()
                                        .id(it.id)
                                        .content(it)
                                        .listener(View.OnClickListener {
                                            feedID = content.id
                                            recycler_view.requestModelBuild()
                                            showSoftKeyboard(edit_content)
                                        })
                                        .onLiked(View.OnClickListener {
                                            contentList[content.id - 1] = Content(content.id, content.by, content.content, contentList[content.id - 1].likeCount + 1, content.feedID, content.commentID)
                                            recycler_view.requestModelBuild()
                                        })
                                        .addTo(controller)
                            }
                        }
                    }
                }

            }
        }
    }


    override fun onBackPressed() {
        when {
            commentID != -1 -> {
                commentID = -1
                recycler_view.requestModelBuild()
            }
            feedID != -1 -> {
                feedID = -1
                recycler_view.requestModelBuild()
            }
            else -> super.onBackPressed()
        }
    }

    fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
