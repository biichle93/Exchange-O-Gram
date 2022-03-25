package com.example.exchange_o_gram

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.parse.LogInCallback;
import com.parse.ParseException

import com.parse.ParseObject
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()

        //login listener
        btnLogin.setOnClickListener(){
            //Toast.makeText(this, "Login Button works as expected", Toast.LENGTH_SHORT).show()
            val userName = findViewById<EditText>(R.id.etUsername).text.toString()
            val passWord = findViewById<EditText>(R.id.etPassword).text.toString()
            login(userName,passWord)
        }
        btnSignup.setOnClickListener(){
            //Toast.makeText(this, "Signup Button works as expected", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(userName: String, passWord: String) {
        ParseUser.logInInBackground(userName,passWord){parseUser: ParseUser?, parseException: ParseException? ->
            if(parseUser != null){
                Toast.makeText(this, "Welcome Back $userName", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                ParseUser.logOut()
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                if(parseException != null){Toast.makeText(this, parseException.message, Toast.LENGTH_SHORT).show()}
            }
        }

    }

    private fun setupView() {
        // find views and buttons
        val title = findViewById<TextView>(R.id.tvLogo)
        val mantra = findViewById<TextView>(R.id.tvMantra2)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        // sets font resources
        title.typeface = ResourcesCompat.getFont(this, R.font.instafont_regular)
        mantra.typeface = ResourcesCompat.getFont(this, R.font.instafont_bold)
        title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}