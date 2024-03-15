package com.example.app_quan_ly_benh_vien_fix

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import kotlinx.android.synthetic.main.activity_screen_add_in_sure_rance.progress_loadWebHeathInsurerance
import kotlinx.android.synthetic.main.activity_screen_add_in_sure_rance.webViewHeathInsureracne

class ScreenAddInSureRance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_add_in_sure_rance)
        khoi_tao_webview()
    }

    private fun khoi_tao_webview() {
        val countTime = object : CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
                val time = ((3000 - p0)/50).toInt()
                progress_loadWebHeathInsurerance.progress = time
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onFinish() {
                progress_loadWebHeathInsurerance.visibility = View.GONE
                webViewHeathInsureracne.visibility = View.VISIBLE
                webViewHeathInsureracne.loadUrl("https://dichvucong.gov.vn/p/home/dvc-trang-chu.html")
                webViewHeathInsureracne.settings.javaScriptEnabled = true
                webViewHeathInsureracne.settings.useWideViewPort = true
            }
        }
        countTime.start()
    }
}