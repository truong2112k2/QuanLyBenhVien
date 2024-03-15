package admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_quan_ly_benh_vien_fix.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_view_tim_mach.recycle_timMachData

class ScreenViewTimMach : AppCompatActivity() {
    private lateinit var db : DatabaseReference
    private lateinit var list: ArrayList<DataDepartment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view_tim_mach)
        db = FirebaseDatabase.getInstance().getReference("Khoa tim mạch")
        list = ArrayList<DataDepartment>()
        recycle_timMachData.layoutManager = LinearLayoutManager(this@ScreenViewTimMach)
        show_data()

    }

    private fun show_data() {
        db.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot.exists()){
                    for( i in snapshot.children){
                        val data = i.getValue(DataDepartment :: class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    val adt = RecycleShowDrInDepartment(list)
                    recycle_timMachData.adapter = adt
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ScreenViewTimMach, "Không thể kết nối đến Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }
}