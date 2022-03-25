package com.example.exchange_o_gram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.Toolbar)

        queryPosts()

    }

    private fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        // Find all posts
        query.findInBackground(object: FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null){
                    //something has went wrong
                    Log.e(TAG, "Error finding posts")
                }else{
                    if(posts != null){
                        for(post in posts){
                           Log.i(TAG, "Post: ${post.getDescription()}")
                        }
                    }
                }
            }

        })
    }
    companion object{
        const val TAG = "MainActivity"
    }

    //called from XML in toolbar button
    fun returnToLogin(v: View?){
        Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
        ParseUser.logOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}