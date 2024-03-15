package com.example.app_quan_ly_benh_vien_fix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.progressLoadingScreenSecond

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        khoitaoprogressbarLoading()
    }
    private fun khoitaoprogressbarLoading() {
        val count  = object : CountDownTimer(5000, 1000){
            override fun onTick(p0: Long) {
                val pro = ((5000-p0)/50).toInt()
                progressLoadingScreenSecond.progress = pro
            }

            override fun onFinish() {
                val i = Intent(this@MainActivity, ScreenSecond :: class.java)
                startActivity(i)
                finish()
            }
        }
        count.start()
    }
}