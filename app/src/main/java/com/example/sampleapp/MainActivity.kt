package com.example.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import javax.inject.Inject
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    @Inject lateinit var car:Car
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerCarComponent.create().inject(this)
        car.start()
        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load("https://i.gifer.com/Buo.gif").into(imageView)
    }
}
