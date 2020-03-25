package com.example.android.ecotown.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android.ecotown.Adapters.CommentAdapter
import com.example.android.ecotown.Models.Comment
import com.example.android.ecotown.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class PostDetailActivity : AppCompatActivity() {
    private lateinit var bindingPostDetailActivity: ActivityPostDetailBinding
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var postKey: String

    lateinit var comments: MutableList<Comment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPostDetailActivity = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(bindingPostDetailActivity.root)

        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        database = FirebaseDatabase.getInstance()

        postKey = intent.extras?.getString("postKey").toString()

        bindingPostDetailActivity.detailAddCommentBtn.setOnClickListener {
            bindingPostDetailActivity.progressBarComment.visibility = View.VISIBLE
            bindingPostDetailActivity.detailAddCommentBtn.visibility = View.INVISIBLE
            val reference: DatabaseReference =
                database.getReference("Comment").child(postKey).push()
            val commentContent = bindingPostDetailActivity.detailComment.text.toString()
            val uID = user?.uid
            val uName = user?.displayName
            val comment = Comment(commentContent, uID.toString(), uName.toString())

            reference.setValue(comment).addOnSuccessListener {
                Toast.makeText(this, "Комментарий добавлен", Toast.LENGTH_SHORT).show()
                bindingPostDetailActivity.detailComment.text.clear()
                bindingPostDetailActivity.progressBarComment.visibility = View.INVISIBLE
                bindingPostDetailActivity.detailAddCommentBtn.visibility = View.VISIBLE
            }.addOnFailureListener {
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
            }

        }


        val postImage = intent.extras?.getString("postImage")
        Glide.with(this).load(postImage).into(bindingPostDetailActivity.detailImg)
        val postTitle = intent.extras?.getString("title")
        bindingPostDetailActivity.detailTitle.text = postTitle

        val postDescription = intent.extras?.getString("description")
        bindingPostDetailActivity.detailDescription.text = postDescription


        val postName = intent.extras?.getString("postName")
        bindingPostDetailActivity.detailUserName.text = user?.displayName

        val postDate = intent.extras?.getLong("time")

        val dateName = timeStampToString(postDate) + " || by " + postName
        bindingPostDetailActivity.detailDate.text = dateName

        iniComment()
    }

    private fun iniComment() {
        bindingPostDetailActivity.commentRecycler.layoutManager = LinearLayoutManager(this)
        val commentReference = database.getReference("Comment").child(postKey)
        val commentListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                comments = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val comment = postSnap.getValue(Comment::class.java)
                    comments.add(comment as Comment)
                }
                val commentAdapter = CommentAdapter(comments)
                bindingPostDetailActivity.commentRecycler.adapter = commentAdapter

            }
        }
        commentReference.addValueEventListener(commentListener)

    }

    private fun timeStampToString(s: Long?): String {
        val format = SimpleDateFormat("hh:mm")
        return format.format(s)
    }
}
