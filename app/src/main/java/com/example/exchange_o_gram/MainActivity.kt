package com.example.exchange_o_gram

import android.content.Intent
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.parse.ParseUser


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.Toolbar)
        val btnLogout = findViewById<Button>(R.id.logout)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        var selected = false
        val id =item.itemId
        if (id == R.id.logout) {
            selected = true
        }
        return selected
    }
    fun returnToLogin(v: View?){
        Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
        ParseUser.logOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}