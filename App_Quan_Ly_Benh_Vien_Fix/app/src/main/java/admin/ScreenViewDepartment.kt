package admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.app_quan_ly_benh_vien_fix.R
import kotlinx.android.synthetic.main.activity_screen_view_department.btnHoHap
import kotlinx.android.synthetic.main.activity_screen_view_department.btnHoiSuc
import kotlinx.android.synthetic.main.activity_screen_view_department.btnKhoaNhi
import kotlinx.android.synthetic.main.activity_screen_view_department.btnKhoaSan
import kotlinx.android.synthetic.main.activity_screen_view_department.btnNoiTiet
import kotlinx.android.synthetic.main.activity_screen_view_department.btnTimMach


class ScreenViewDepartment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view_department)

        xetSuKienKhiClick()
    }

    private fun xetSuKienKhiClick() {
        val ani = AnimationUtils.loadAnimation(this@ScreenViewDepartment , R.anim.animation)
         btnHoHap.setOnClickListener {
             btnHoHap.startAnimation(ani) // xét hiệu ứng
             val i = Intent(this@ScreenViewDepartment, ScreenViewHoHap :: class.java)
             startActivity(i) // chuyển activity
         }
        btnNoiTiet.setOnClickListener {
            btnNoiTiet.startAnimation(ani) // xét hiệu ứng
            val i = Intent(this@ScreenViewDepartment, ScreenViewNoiTiet :: class.java)
            startActivity(i) // chuyển activity
        }

        btnHoiSuc.setOnClickListener {
            btnHoiSuc.startAnimation(ani) // xét hiệu ứng
            val i = Intent(this@ScreenViewDepartment, ScreenViewHoiSuc :: class.java)
            startActivity(i) // chuyển activity
        }

        btnKhoaNhi.setOnClickListener {
            btnKhoaNhi.startAnimation(ani) // xét hiệu ứng
            val i = Intent(this@ScreenViewDepartment, ScreenViewKhoaNhi :: class.java)
            startActivity(i) // chuyển activity
        }
        btnTimMach.setOnClickListener {
            btnTimMach.startAnimation(ani) // xét hiệu ứng
            val i = Intent(this@ScreenViewDepartment, ScreenViewTimMach :: class.java)
            startActivity(i) // chuyển activity
        }
        btnKhoaSan.setOnClickListener {
            btnKhoaSan.startAnimation(ani) // xét hiệu ứng
            val i = Intent(this@ScreenViewDepartment, ScreenViewKhoaPhuSan :: class.java)
            startActivity(i) // chuyển activity
        }
    }
}