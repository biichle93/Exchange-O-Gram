package com.example.exchange_o_gram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        listeners()
    }
    fun listeners(){
        //grab references to things we're listening to
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        //set listeners to the referenced entities
        btnCancel.setOnClickListener(){
            finish()
        }
    }
}