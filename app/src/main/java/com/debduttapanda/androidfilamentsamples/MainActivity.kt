package com.debduttapanda.androidfilamentsamples

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.bt_hello_triangle).setOnClickListener {
            startActivity(Intent(this,HelloTriangle::class.java))
        }
        findViewById<Button>(R.id.bt_minimal).setOnClickListener {
            startActivity(Intent(this,MinimalActivity::class.java))
        }
        findViewById<Button>(R.id.bt_camera).setOnClickListener {
            startActivity(Intent(this,HelloCamera::class.java))
        }
    }
}