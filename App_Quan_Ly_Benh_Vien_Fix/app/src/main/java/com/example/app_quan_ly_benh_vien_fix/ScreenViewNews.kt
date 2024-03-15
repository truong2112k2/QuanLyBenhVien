package com.example.app_quan_ly_benh_vien_fix

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_screen_view_news.progres_loadNews
import kotlinx.android.synthetic.main.activity_screen_view_news.view.progres_loadNews
import kotlinx.android.synthetic.main.activity_screen_view_news.webViewNews

class ScreenViewNews : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view_news)
        khoi_tao_webview()


    }

    private fun khoi_tao_webview() {

        val countTime = object : CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
                val time = ((3000-p0)/50).toInt()
                progres_loadNews.progress = time
            }

            @SuppressLint("SetJavaScriptEnabled")
            // take note và tìm hiểu thêm các hàm để settings webview
            override fun onFinish() {
                    progres_loadNews.visibility = View.GONE
                    webViewNews.visibility = View.VISIBLE
                    webViewNews.loadUrl("https://dantri.com.vn/suc-khoe.htm")
                    webViewNews.settings.javaScriptEnabled = true // cho phép hiển thị Javascript
                    webViewNews.settings.useWideViewPort = true // cho phép hiên thị toàn bộ trang web
            }
        }
        countTime.start()
    }
}