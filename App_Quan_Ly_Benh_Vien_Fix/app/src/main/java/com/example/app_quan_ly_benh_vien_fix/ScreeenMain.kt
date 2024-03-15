package com.example.app_quan_ly_benh_vien_fix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_screeen_main.BigLinear
import kotlinx.android.synthetic.main.activity_screeen_main.btnService
import kotlinx.android.synthetic.main.activity_screeen_main.btnViewNews
import kotlinx.android.synthetic.main.activity_screeen_main.btnViewBookCalendar
import kotlinx.android.synthetic.main.activity_screeen_main.btnViewDr
import kotlinx.android.synthetic.main.activity_screeen_main.cardView

// thiết kế imageview hình tròn
class ScreeenMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screeen_main)
        setThoiGianHienThi()
        setChuyenManHinh()


    }

    private fun setChuyenManHinh() {
        val id = intent.getStringExtra("idnguoidung")
        // animation
        val ani = AnimationUtils.loadAnimation(this@ScreeenMain, R.anim.animation)
        btnViewDr.setOnClickListener {
            btnViewDr.startAnimation(ani)
            // chuyển màn hình xem bác sĩ
            val i = Intent(this@ScreeenMain, ScreenViewDr :: class.java)
            i.putExtra("idnguoidung2",id)
            startActivity(i)
        }
        btnViewBookCalendar.setOnClickListener {
            btnViewBookCalendar.startAnimation(ani)
            val id = intent.getStringExtra("idnguoidung")
            // Chuyển màn hình xem lịch khám
            val i = Intent(this@ScreeenMain, ScreenViewCalendarBook :: class.java)
            i.putExtra("idnguoidung2",id)
            startActivity(i)
        }
        btnViewNews.setOnClickListener {
            btnViewNews.startAnimation(ani)
            val i = Intent(this@ScreeenMain, ScreenViewNews :: class.java)
            startActivity(i)
        }
        btnService.setOnClickListener {
            btnService.startAnimation(ani)
            val i = Intent(this@ScreeenMain, ScreenAddInSureRance :: class.java)
            startActivity(i)
        }

    }

    private fun setThoiGianHienThi() {
        val countimeCard = object : CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                cardView.visibility = View.VISIBLE
            }
        }
        countimeCard.start()



        val countime2 = object : CountDownTimer(6000, 1000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                BigLinear.visibility = View.VISIBLE
            }
        }
        countime2.start()
    }


}
