package admin

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import com.example.app_quan_ly_benh_vien_fix.R
import kotlinx.android.synthetic.main.screen_admin_home.btnManagerDoctor
import kotlinx.android.synthetic.main.screen_admin_home.btnManagerMedicalDepartment
import kotlinx.android.synthetic.main.screen_admin_home.progressbar_loading

class ScreenAdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_admin_home)
        cai_dat_view()
        khoi_tao_thao_tac()

    }

    private fun khoi_tao_thao_tac() {
        val animation = AnimationUtils.loadAnimation(this@ScreenAdminHome, R.anim.animation)

        btnManagerDoctor.setOnClickListener {
            btnManagerDoctor.startAnimation(animation)
            val i = Intent(this@ScreenAdminHome, ScreenManagerDoctor :: class.java)
            startActivity(i)
        }


        btnManagerMedicalDepartment.setOnClickListener {
            btnManagerMedicalDepartment.startAnimation(animation)
            val i = Intent(this@ScreenAdminHome, ScreenViewDepartment :: class.java)
            // biến từ màn hình ScreenAdminHome -> màn hình ScreenViewDepartment
            startActivity(i) // cho biến i khởi chạy
        }



    }

    private fun cai_dat_view() {
        val timedown = object : CountDownTimer(5000, 1000){
            override fun onTick(p0: Long) {
                val progress = ((5000- p0)/50).toInt()
                progressbar_loading.progress = progress
            }

            override fun onFinish() {
               progressbar_loading.visibility = View.GONE
                btnManagerDoctor.visibility  = View.VISIBLE
                btnManagerMedicalDepartment.visibility =   View.VISIBLE


            }
        }.start()
    }
}