package com.example.exchange_o_gram

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SignUpCallback;

class SignupActivity : AppCompatActivity() {

    lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setupView()
        btnConfirm.setOnClickListener(){
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            signup(username, password)
        }
    }

    private fun signup(username: String, password: String) {
        val user = ParseUser()
        user.username = username
        user.setPassword(password)
        user.signUpInBackground(SignUpCallback {
            if(it == null){
                Toast.makeText(this,"Successful! Welcome, $username", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                ParseUser.logOut()
                Toast.makeText(this,"Signup Failed! ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupView() {
        // find views and buttons
        val title = findViewById<TextView>(R.id.tvTitle)
        val mantra = findViewById<TextView>(R.id.tvMantra2)
        btnConfirm = findViewById<Button>(R.id.btnConfirm)

        // sets font resources
        title.typeface = ResourcesCompat.getFont(this, R.font.instafont_regular)
        mantra.typeface = ResourcesCompat.getFont(this, R.font.instafont_bold)
        title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}