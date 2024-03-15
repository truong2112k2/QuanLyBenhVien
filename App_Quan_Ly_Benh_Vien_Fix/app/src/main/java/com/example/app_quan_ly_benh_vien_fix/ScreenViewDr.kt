package com.example.app_quan_ly_benh_vien_fix

import admin.DataDoctor
import admin.setOnClick
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_screen_view_dr.recycle_showDrtoUser
import kotlinx.android.synthetic.main.activity_screen_view_dr.searchview_findDrUser

class ScreenViewDr : AppCompatActivity() {
    private lateinit var db : DatabaseReference
    private lateinit var list: ArrayList<DataDoctor>
    private lateinit var alertDialog: AlertDialog
    private lateinit var alertDialogRate: AlertDialog
    private lateinit var adt: RecycleShowDrUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view_dr)
        db = FirebaseDatabase.getInstance().getReference("DATA_DOCTOR")
        list = ArrayList<DataDoctor>()
        recycle_showDrtoUser.layoutManager = LinearLayoutManager(this@ScreenViewDr)
        show_du_lieu()
        ham_tim_kiem_bac_si()
    }

    private fun ham_tim_kiem_bac_si() {
        searchview_findDrUser.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tim_kiem_bac_si(p0!!)
                return true
            }
        })
    }

    private fun show_du_lieu() {
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot.exists()){
                    for( i in snapshot.children){
                        val data = i.getValue(DataDoctor :: class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    adt = RecycleShowDrUser(list, object : setOnClick{
                        @SuppressLint("InflateParams", "MissingInflatedId", "SetTextI18n")
                        override fun setClick(postition: Int) { // sự kiện khi click item Recycleview
                            val build = AlertDialog.Builder(this@ScreenViewDr)
                            val view =layoutInflater.inflate(R.layout.alertdialog_idetail_drforuser, null )
                            build.setView(view)
                            // ánh xạ view
                            val txtShowName = view.findViewById<TextView>(R.id.txtShowNameAlertDetail)
                            val txtShowAge = view.findViewById<TextView>(R.id.txtShowAgeAlertDetail)
                            val txtShowtype = view.findViewById<TextView>(R.id.txtShowTypeOfDeleaseAlertDetail)
                            val showimage = view.findViewById<ImageView>(R.id.imgShowImageAlertDetailDr)
                            val btnBook = view.findViewById<Button>(R.id.btnBookDr)
                            val btnRate = view.findViewById<Button>(R.id.btnRateDr)
                            // gán nội dung và xét sự kiện
                            txtShowName.text = list[postition].drName
                            txtShowAge.text = list[postition].drAge
                            txtShowtype.text = list[postition].typeOfDesease
                            Picasso.get().load(list[postition].imageDr).into(showimage)


                            btnBook.setOnClickListener {
                                /*
                               khi click vào book dr sẽ chuyển đến 1 activity cho phép ng dùng đặt lịch
                               tiếp theo đó sau khi ng dùng hoàn thành chọn ngày, giờ t sẽ thêm 1 node(lịch khám)
                                vào node Tài khảo ng dùng thông qua id
                                Nhớ mang id theo nhé thằng ngu ơi 21/2/2024
                                 */
                                // lấy id người dùng
                                val idnguoidung = intent.getStringExtra("idnguoidung2")
                                // lấy data bác sĩ
                                val dataDr = list[postition]
                                val i = Intent(this@ScreenViewDr, ScreenBookDr::class.java)
                                i.putExtra("idnguoidung3", idnguoidung)
                                i.putExtra("dataDr", dataDr ) // gửi dữ liệu dr đến activity sau
                                startActivity(i)
                                finish()
                                alertDialog.dismiss()
                            }
                            btnRate.setOnClickListener {
                            //    Toast.makeText(this@ScreenViewDr, "Chỗ này chưa được học", Toast.LENGTH_SHORT).show()
                                  val build1 = AlertDialog.Builder(this@ScreenViewDr)
                                  val view1 = layoutInflater.inflate(R.layout.alerdialog_rate, null )
                                val drRate = view1.findViewById<TextView>(R.id.txtNameDrRate)
                                val cmtRate = view1.findViewById<EditText>(R.id.edtCmtRate)
                                val barRate = view1.findViewById<RatingBar>(R.id.rateDrBar)
                                val btnSendRate = view1.findViewById<Button>(R.id.btnSendRate)
                                   drRate.text ="${list[postition].drName}"
                                var num: Float = 0.0f
                                barRate.setOnRatingBarChangeListener { ratingBar, fl, b ->
                                    // fl là số sao được trọn
                                   num = fl
                                }
                                btnSendRate.setOnClickListener {
                                    val dbRate = FirebaseDatabase.getInstance().getReference("DATA_RATE")
                                    val idRate = db.push().key!!
                                    var cmt = "Không có"
                                    if( cmtRate.text.toString().length > 0){
                                        cmt = cmtRate.text.toString()
                                    }
                                    val nameDr = list[postition].drName
                                    val dataRate = DataRate(idRate, nameDr ,cmt, num)
                                    dbRate.child(idRate).setValue(dataRate)
                                        .addOnSuccessListener {
                                            val alertThankYou = AlertDialog.Builder(this@ScreenViewDr)
                                            alertThankYou.apply {
                                                setTitle("Cảm ơn vì phản hồi của bạn ♥")
                                                setNegativeButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                                    dialogInterface.dismiss()
                                                    alertDialogRate.dismiss()
                                                }
                                            }.show()
                                        }

                                }
                                  build1.setView(view1)
                                alertDialogRate = build1.create()
                                alertDialogRate.show()
                                alertDialog.dismiss()

                            }

                            alertDialog = build.create()
                            alertDialog.show()
                        }
                    })
                    recycle_showDrtoUser.adapter = adt
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ScreenViewDr, "Không thể kết nối đến Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun tim_kiem_bac_si(textView: String){
        val newList = ArrayList<DataDoctor>()
        for ( i in list){
            if(i.drName!!.lowercase().contains(textView.lowercase()) == true ){
                newList.add(i)
            }
        }
        adt.changeList(newList)

    }

}