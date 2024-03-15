package admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_quan_ly_benh_vien_fix.DataBook
import com.example.app_quan_ly_benh_vien_fix.R
import com.example.app_quan_ly_benh_vien_fix.RecycleShowCalendarUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_screen_view_book_calendar_admin.recycle_showBookCalendarforAdmin

class ScreenViewBookCalendarAdmin : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var list: ArrayList<DataBook>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view_book_calendar_admin)
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC").child("DATA_BOOK")

        list = ArrayList<DataBook>()
        recycle_showBookCalendarforAdmin.layoutManager = LinearLayoutManager(this@ScreenViewBookCalendarAdmin)
        show_du_lieu_len_recycleview()
    }

    private fun show_du_lieu_len_recycleview() {
        db.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for( childsnap in snapshot.children){

//                    val namePatient = childsnap.child("patientName").getValue(String:: class.java)
//                    val gmailPatient = childsnap.child("patientGmail").getValue(String:: class.java)
//                    val phonePatient = childsnap.child("patientNumber").getValue(String:: class.java)
//                    val idPatient = childsnap.child("idBook").getValue(String:: class.java)
//                    val time = childsnap.child("time").getValue(String:: class.java)
//                    val date = childsnap.child("date").getValue(String:: class.java)
//                    val dr = childsnap.child("doctor").getValue(DataDoctor:: class.java)
//                    if(   namePatient != null && gmailPatient != null &&
//                          phonePatient != null && idPatient != null &&
//                           time != null && date != null && dr != null){
//                        val dataBook = DataBook(idPatient,namePatient, phonePatient, gmailPatient, date,time,dr)
//                        list.add(dataBook)
//                    }
                    for ( i in snapshot.children){
                        val data = i.getValue(DataBook :: class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    val adt = RecycleShowCalendarUser(list, object : setOnClick{
                        override fun setClick(postition: Int) {
                            // xét hành động khi click
                        }
                    })
                    recycle_showBookCalendarforAdmin.adapter = adt

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}