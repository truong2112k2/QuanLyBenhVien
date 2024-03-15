package com.example.app_quan_ly_benh_vien_fix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_screen_second.btnStart

class ScreenSecond : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_second)
        chuyenmanhinhhome()
    }

    private fun chuyenmanhinhhome() {
        val ani = AnimationUtils.loadAnimation(this@ScreenSecond, R.anim.animation)
        btnStart.setOnClickListener {
            btnStart.startAnimation(ani)
            val i = Intent(this@ScreenSecond, ScreenHome :: class.java)
            startActivity(i)
            finish()

        }
    }
}