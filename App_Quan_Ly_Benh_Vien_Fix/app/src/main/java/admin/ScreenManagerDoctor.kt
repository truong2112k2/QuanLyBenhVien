package admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_quan_ly_benh_vien_fix.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_manager_doctor.btnAdd_Doctor
import kotlinx.android.synthetic.main.activity_screen_manager_doctor.recycle_showDataDrForAddmin
import kotlinx.android.synthetic.main.activity_screen_manager_doctor.searchview_followName
import kotlinx.android.synthetic.main.item_recycleview_manager_update_doctor.edtInputUpdateImageDr

class ScreenManagerDoctor : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var list: ArrayList<DataDoctor>
    private lateinit var alerDiaLog : AlertDialog
    private lateinit var adapter : RecycleShowDrAdapterForAdmin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_manager_doctor)
        show_infoDoctor()
        khoi_tao_nut_them_bac_si()
        tim_kiem_bac_si_theo_ten()



    }

    private fun tim_kiem_bac_si_theo_ten() {
        searchview_followName.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tim_kiem_bac_si(p0!!)
                return true
            }
        })

    }

    private fun show_infoDoctor() {
        db = FirebaseDatabase.getInstance().getReference("DATA_DOCTOR") //  tạo Firebase
        // tạo arraylist-> đây sẽ là ArrayList t truyền vào Adapter nhằm hiển thị thông tin lên Recycleview
        list = ArrayList<DataDoctor>()
        // Cài đặt hiển thị cho Recycleview
        recycle_showDataDrForAddmin.layoutManager = LinearLayoutManager(this@ScreenManagerDoctor)
        // Thực hiện lấy dữ liệu từ Firebase
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){ // nếu như node có tồn tại
                    for( i in snapshot.children){ // t sẽ dùng vòng lặp for đi từng phần tử của node
                        // ở mỗi vị trí mà vòng for đi qua, t sẽ gán dữ liệu lấy được vào biến data và ép kiểu cho nó là DataDoctor thông qua hàm getValue
                        val data = i.getValue(DataDoctor :: class.java)
                        list.add(data!!) // add dữ liệu vừa lấy được vào arraylist mà t tạo
                    }

                    adapter = RecycleShowDrAdapterForAdmin(list, object : setOnClick{ // xét sự kiện khi click vào item trên Recycleview
                        @SuppressLint("InflateParams", "MissingInflatedId")
                        override fun setClick(postition: Int) {

                         // sau khi click vào item-> t sẽ hiển thị lên 1 alerdialog cho phép ng dùng xóa và sửa thông tin bác sĩ
                        val build = AlertDialog.Builder(this@ScreenManagerDoctor)
                         // chuyển file xml thành view để tương tác
                        val view = layoutInflater.inflate(R.layout.item_recycleview_manager_update_doctor, null )
                        build.setView(view)
                            // ảnh xạ các view
                         val edtUpdateName = view.findViewById<EditText>(R.id.edtInputUpdateName)
                         val edtUpdateAge = view.findViewById<EditText>(R.id.edtInputUpdateAge)
                         val edtDepartment = view.findViewById<EditText>(R.id.edtInputUpdateDepartment)
                         val edtType = view.findViewById<EditText>(R.id.edtInputUpdateTypeofDelase)
                         val edtUpdateImage = view.findViewById<EditText>(R.id.edtInputUpdateImageDr)
                         val btnDelete = view.findViewById<Button>(R.id.btnDelete)
                         val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

                         edtUpdateName.setText(list[postition].drName)
                         edtUpdateAge.setText(list[postition].drAge)
                         edtDepartment.setText(list[postition].medicalDepartment)
                         edtType.setText(list[postition].typeOfDesease)
                         edtUpdateImage.setText(list[postition].imageDr)

                             // xóa dữ liệu
                            btnDelete.setOnClickListener { // nếu nút delete được click
                                // truy cập đến vị trí của item trên firebase bằng ID của nó
                                val dbref = FirebaseDatabase.getInstance().getReference("DATA_DOCTOR").child(list[postition].id.toString())
                                val mtas =dbref.removeValue() // sử dụng hàm removeValue() để xóa item
                                    . addOnSuccessListener {
                                        // thông báo xóa thành công
                                        Toast.makeText(this@ScreenManagerDoctor, "Xóa thành công", Toast.LENGTH_SHORT).show()
                                        alerDiaLog.dismiss() // cho hộp thoại ẩn đi
                                        val i = Intent(this@ScreenManagerDoctor, LoadingActivity :: class.java) // chuyển màn hình đến màn hình loading để update data lên Recycleview
                                        startActivity(i)

                                    }
                                    .addOnFailureListener {
                                        // thông báo khi xóa thất bại
                                        Toast.makeText(this@ScreenManagerDoctor, "Xóa thất bại", Toast.LENGTH_SHORT).show()

                                    }

                            }
                           // cập nhật dữ liệu
                            btnUpdate.setOnClickListener {
                                // truy cập dữ liệu của phần tử t muốn cập nhật trên Firebase thông qua ID của nó
                                val dbref2 = FirebaseDatabase.getInstance().getReference("DATA_DOCTOR").child(list[postition].id.toString())
                                // lấy dữ liệu từ các editext
                                val name = edtUpdateName.text.toString()
                                val age = edtUpdateAge.text.toString()
                                val department = edtDepartment.text.toString()
                                val type = edtType.text.toString()
                                val image = edtUpdateImage.text.toString()
                                // tạo 1 biến kiểu DataDoctor từ các dữ liệu vừa lấy (đây là dữ liệu mới để cập nhật)
                                val dataDrUpdate =  DataDoctor(list[postition].id.toString(), name, age, department, type,image)
                                // cập nhật dữ liệu mới lên Firebase bằng hàm setValue
                                dbref2.setValue(dataDrUpdate)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@ScreenManagerDoctor, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@ScreenManagerDoctor, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                                    }
                                val i = Intent(this@ScreenManagerDoctor, LoadingActivity :: class.java)
                                startActivity(i)
                            }
                        alerDiaLog = build.create()
                        alerDiaLog.show() // cho hộp thoại hiển thị
                        }
                    })
                    recycle_showDataDrForAddmin.adapter = adapter // gán adapter cho view Recycleview để hiển thị dữ liệu leên Firebase
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun khoi_tao_nut_them_bac_si() { // khi ng dùng click vào button add bác sĩ
        val ani = AnimationUtils.loadAnimation( this@ScreenManagerDoctor,R.anim.animation)
        btnAdd_Doctor.setOnClickListener {
            btnAdd_Doctor.startAnimation(ani)
            val i = Intent(this@ScreenManagerDoctor, ScreenManagerDoctor_Add :: class.java) // chuyển màn hình activity add bác sĩ
            startActivity(i)
        }
    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }
    override fun recreate() {
        super.recreate()
    }
    fun tim_kiem_bac_si(text: String){
        val searchList = ArrayList<DataDoctor>() // tạo ra 1 list mới
        for( dataClass in list){   // duyệt list ban đầu
            if(dataClass.drName?.lowercase()?.contains(text.lowercase()) == true ){
                // đi 1 vòng kiểm tra xem text truyền vào có trùng với item nào không
                searchList.add(dataClass) // nếu trùng t sẽ add nó vào list mới tạo ra
            }
        }
        adapter.searchDataList(searchList) // thay list cũ bằng list mới để nó hiển thị ra kết quả tìm kiếm

    }
}