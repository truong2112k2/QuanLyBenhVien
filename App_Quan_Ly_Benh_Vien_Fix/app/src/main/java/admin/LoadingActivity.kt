package admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.app_quan_ly_benh_vien_fix.R
import kotlinx.android.synthetic.main.activity_loading2.prLoading

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading2)

        val countTime = object : CountDownTimer(3000, 3000){
            override fun onTick(p0: Long) {
                val progress = ((5000 - p0)/50).toInt()

                prLoading.progress = progress
            }

            override fun onFinish() {
               val i = Intent(this@LoadingActivity, ScreenManagerDoctor :: class.java)
                startActivity(i)
            }
        }
        countTime.start()

    }
}