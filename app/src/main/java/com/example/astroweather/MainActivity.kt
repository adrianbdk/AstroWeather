package com.example.astroweather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        buttonSettings.setOnClickListener(){
            startActivity(Intent(this, Settings::class.java))
        }

        buttonAstro.setOnClickListener(){
            startActivity(Intent(this, AstroCalculator::class.java))
        }

    }
}