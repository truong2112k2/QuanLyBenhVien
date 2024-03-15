package admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.PixelCopy.Request
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_quan_ly_benh_vien_fix.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.btnSaveDataDoctor
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.edtInputDoctorAge
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.SpnInputDoctorMedicalDepartment
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.btnAddImageDoctor
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.edtInputDoctorName
import kotlinx.android.synthetic.main.activity_screen_manager_doctor_add.spnInputTypeOfDesease



class ScreenManagerDoctor_Add : AppCompatActivity() {
    private val REQUEST_IMAGE_PICK = 1
   lateinit var alert_addImage: AlertDialog

    var nameDepartment = "null"
    var nameType = "null"
     var imageDr = "null"
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_manager_doctor_add)
        khoitaospinner()
        addImageDr()
        addDataDr()
    }

    // code nút chọn ảnh
    @SuppressLint("InflateParams")
    private fun addImageDr() {
        btnAddImageDoctor.setOnClickListener {
            val build = AlertDialog.Builder(this@ScreenManagerDoctor_Add)
            val view = layoutInflater.inflate(R.layout.alerdialog_select_image, null)
            build.setView(view)
            alert_addImage = build.create()
            val edtLinkAnh = view.findViewById<EditText>(R.id.edtInPutLinkImage)
            val btnComFirm = view.findViewById<Button>(R.id.btnComfirmLinkImage)
            btnComFirm.setOnClickListener {
                if(checkLinkImage(edtLinkAnh)){
                    imageDr = edtLinkAnh.text.toString()
                    alert_addImage.dismiss()
                }else{
                    edtLinkAnh.error ="Đường dẫn không hợp lệ"
                }

            }

            alert_addImage.show()

        }
    }
    // kiểm tra xem ng dùng có nhập vào 1 link ảnh
    private fun checkLinkImage(editText: EditText): Boolean {
        // Kiểm tra logic của bạn để xác định xem đường link ảnh có hợp lệ hay không
        // Ví dụ: Kiểm tra xem đường link có bắt đầu bằng "http://" hoặc "https://" không
//        return url.startsWith("http://") || url.startsWith("https://")
        val url = editText.text.toString()
        if( url.startsWith("http://") || url.startsWith("https://")){
            return true
        }else{
            return false
        }
    }






    // khởi tạo spinner để chọn khoa và loại bệnh
    private fun khoitaospinner() {

        // khởi tạo các khoa bệnh
        val arrayDepartment = ArrayList<String>()
        arrayDepartment.add("1 - Khoa nội tiết")
        arrayDepartment.add("2 - Khoa hô hấp")
        arrayDepartment.add("3 - Khoa tim mạch")
        arrayDepartment.add("4 - Khoa hồi sức")
        arrayDepartment.add("5 - Khoa nhi")
        arrayDepartment.add("6 - Khoa phụ sản")

        val adapterDepartMent =  ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1,arrayDepartment)
        SpnInputDoctorMedicalDepartment.adapter = adapterDepartMent

        val noiTietList = ArrayList<String>()
        val hoHapList = ArrayList<String>()
        val timMachList = ArrayList<String>()
        val hoiSucList = ArrayList<String>()
        val khoaNhiList = ArrayList<String>()
        val phuSanList = ArrayList<String>()



        // khởi tạo các loại bệnh tương ứng với các khoa
        noiTietList.add("1-Tiểu đường")
        noiTietList.add("2-Mỡ máu")
        noiTietList.add("3-Tuyến giáp")

        hoHapList.add("1-Hen xuyễn")
        hoHapList.add("2-Viêm phế quản-mãn tính")
        hoHapList.add("3-Tắc phổi mãn tính")


        timMachList.add("1-Van tim")
        timMachList.add("2-Nhồi máu cơ tim")
        timMachList.add("3-Huyết áp cao")


        hoiSucList.add("1-Tai")
        hoiSucList.add("2-Đa chấn thương")
        hoiSucList.add("3-Suy hô hấp")

        khoaNhiList.add("1-Viêm phổi do vi rút RS")
        khoaNhiList.add("2-Nhiễm khuẩn đường tiểu")
        khoaNhiList.add("3-Sởi")

        phuSanList.add("1-Hiếm muộn")
        phuSanList.add("2-Tiền sản giật")
        phuSanList.add("3-Bệnh lý thai kì")

        SpnInputDoctorMedicalDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                       if( p2 == 0){
                            nameDepartment = "Khoa nội tiết"
                            val adapterNoiTiet = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, noiTietList)
                            spnInputTypeOfDesease.adapter = adapterNoiTiet
                            setNameType("Tiểu đường","Mỡ máu", "Tuyến giáp")
                       }else if( p2 ==1 ){
                           nameDepartment = "Khoa hô hấp"
                           val adapterHoHap = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, hoHapList)
                           spnInputTypeOfDesease.adapter = adapterHoHap
                           setNameType("Hen xuyễn","Viêm phế quản","Tắc phổi mãn tính")
                       }else if( p2 ==2 ){
                           nameDepartment = "Khoa tim mạch"
                           val adapterTimMach = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, timMachList)
                           spnInputTypeOfDesease.adapter = adapterTimMach
                           setNameType("Van tim","Nhồi máu cơ tim", "Huyến áp cao")
                       }else if( p2 ==3 ){
                           nameDepartment = "Khoa hồi sức"
                           val adapterHoiSuc = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, hoiSucList)
                           spnInputTypeOfDesease.adapter = adapterHoiSuc
                           setNameType("Tai","Đa chấn thương","Suy hô hấp")
                       }else if( p2 ==4 ){
                           nameDepartment = "Khoa nhi"
                           val adapterKhoaNhi = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, khoaNhiList)
                           spnInputTypeOfDesease.adapter = adapterKhoaNhi
                           setNameType("Viên phổi","Nhiễm khuẩn đường tiểu","Sởi")
                       }else if( p2 ==5 ){
                           nameDepartment = "Khoa phụ sản"
                           val adapterPhuSan = ArrayAdapter(this@ScreenManagerDoctor_Add, android.R.layout.simple_expandable_list_item_1, phuSanList)
                           spnInputTypeOfDesease.adapter = adapterPhuSan
                           setNameType("Hiếm muộn","Tiền sản giật","Bệnh lý thai kì")
                       }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


    }



    // Thực hiện addData người dùng
    private fun addDataDr() {
        btnSaveDataDoctor.setOnClickListener {
            if(kiemTraDieuKienNhap(edtInputDoctorName)|| kiemTraDieuKienNhap(edtInputDoctorAge)){


            }else{
                // thêm thông tin Dr vào node Dr
                db = FirebaseDatabase.getInstance().getReference("DATA_DOCTOR")
                val drName = edtInputDoctorName.text.toString()
                val drAge = edtInputDoctorAge.text.toString()
                val drDepartment = nameDepartment
                val drType = nameType
                val drImage = imageDr
                val drID = db.push().key!!
                val dataDr = DataDoctor(drID, drName, drAge, drDepartment, drType, drImage)
                db.child(drID).setValue(dataDr)
                    .addOnSuccessListener {
                        alerdialog("Thêm thành công")
                        edtInputDoctorAge.text.clear()
                        edtInputDoctorName.text.clear()
                    }
                    .addOnFailureListener {
                        alerdialog("Thêm thất bại")
                    }

                // thêm thông tin Dr vào cả node khoa luôn
                val db2  = FirebaseDatabase.getInstance().getReference(nameDepartment)
                val dataDepartment = DataDepartment(drID, dataDr)
                db2.child(drID).setValue(dataDepartment)

            }

        }
    }

    private fun kiemTraDieuKienNhap(editText: EditText): Boolean {
        if( editText.text.toString().length ==0){
            editText.error ="Nhập vào nội dung"
            return true
        }
        return false
    }

    private fun alerdialog(string: String) {
        val dialog = AlertDialog.Builder(this@ScreenManagerDoctor_Add)
        dialog.apply {
            dialog.setTitle(string)
                .setNegativeButton("[Ok]") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }

        }.show()
    }

    private fun setNameType( string1: String , string2: String , string3: String  ){
        spnInputTypeOfDesease.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long,
            ) {
                if( p2 ==0 ){
                    nameType = string1
                }else if(p2 == 1){
                    nameType = string2
                }else if(p2 == 2){
                    nameType = string3
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }


}