package com.example.app_quan_ly_benh_vien_fix

import admin.ScreenAdminHome
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_home.btnCreateAccount
import kotlinx.android.synthetic.main.activity_screen_home.btnLogin
import kotlinx.android.synthetic.main.activity_screen_home.btnResetAccount
import kotlinx.android.synthetic.main.activity_screen_home.btnShowPasswordHome
import kotlinx.android.synthetic.main.activity_screen_home.checkBoxRememberLogin
import kotlinx.android.synthetic.main.activity_screen_home.edtInputPassword
import kotlinx.android.synthetic.main.activity_screen_home.edtInputUserName

class ScreenHome : AppCompatActivity() {
    val adminname = "admin"
    val adminpass = "admin123"
    private lateinit var db: DatabaseReference
    private lateinit var saveData : SharedPreferences // sử dụng sharePreferences để lưu data tạm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_home)
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC")
        khoi_tao_nut_tao_tai_khoan()
        khoi_tao_nut_tao_lai_tai_khoan()
        hienthimatkhau()
        khoi_tao_nut_dang_nhap()


    }

    private fun khoi_tao_nut_dang_nhap() {
        btnLogin.setOnClickListener {
            val Uname = edtInputUserName.text.toString()
            val Upass = edtInputPassword.text.toString()
        if(kiem_tra_noi_dung_nhap_Login(edtInputUserName) || kiem_tra_noi_dung_nhap_Login(edtInputPassword)){

        }else if(Uname == adminname && Upass == adminpass){
            edtInputPassword.text.clear()
            edtInputUserName.text.clear()
            chuyen_man_hinh_admin()

        } else{
            db.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for( i in snapshot.children){
                        val data = i.getValue( DataAccount :: class.java)
                        if( data != null && data.name == Uname && data.password == Upass){
                            val idnguoidung = data.id
                            chuyen_man_hinh_main(idnguoidung.toString())
                            edtInputPassword.text.clear()
                            edtInputUserName.text.clear()
                            return
                        }
                    }
                    Toast.makeText(this@ScreenHome, "Tên hoặc mật khẩu sai", Toast.LENGTH_SHORT).show()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ScreenHome, "Không thể kết nối", Toast.LENGTH_SHORT).show()

                }
            })
        }
        }

    }

    private fun chuyen_man_hinh_admin() {
        val i = Intent(this@ScreenHome, ScreenAdminHome :: class.java)
         startActivity(i)
    }

    private fun chuyen_man_hinh_main(idnguoidung: String) {
        val i = Intent(this@ScreenHome, ScreeenMain :: class.java)
        i.putExtra("idnguoidung", idnguoidung)
        startActivity(i)
    }

    private fun hienthimatkhau() {
        btnShowPasswordHome.setOnClickListener {
            bat_tat_mat_khau(edtInputPassword)
        }
    }

    private fun khoi_tao_nut_tao_lai_tai_khoan() {// code cho nút tạo lại tài khoản
        val string1 = "Tạo lại ngay!"
        val s = SpannableString(string1)
        s.setSpan(UnderlineSpan(),0,13, 1)
        btnResetAccount.setText(s)
        val animation = AnimationUtils.loadAnimation(this@ScreenHome, R.anim.animation)
        btnResetAccount.setOnClickListener {
            btnResetAccount.startAnimation(animation)
            chuyen_man_hinh_tao_lai_tai_khoan()

        }


    }

    private fun chuyen_man_hinh_tao_lai_tai_khoan() { // chuyển màn hình tạo lại tài khoản
        val i = Intent(this@ScreenHome, ScreenResetAccount :: class.java)
        startActivity(i)
    }


    private fun khoi_tao_nut_tao_tai_khoan() { // code cho nút tạo tài khoản
        val string1 = "Tạo tài khoản!"
        val s = SpannableString(string1)
        s.setSpan(UnderlineSpan(),0, 14, 1 )
        btnCreateAccount.setText(s)
        val animation = AnimationUtils.loadAnimation(this@ScreenHome, R.anim.animation)
        btnCreateAccount.setOnClickListener {
            btnCreateAccount.startAnimation(animation)
            chuyen_man_hinh_dang_ky_tai_khoan()
        }
    }

    private fun chuyen_man_hinh_dang_ky_tai_khoan() { // chuyển màn hình đăng ký tài khoản
        val i = Intent(this@ScreenHome, ScreenAddAccount :: class.java)
        startActivity(i)
    }
    private var isPasswordVisible = false
    private fun bat_tat_mat_khau(editText: EditText) { // bật tắt hiện thị mật khẩu
        isPasswordVisible = !isPasswordVisible

        // Thay đổi kiểu input dựa trên trạng thái hiện tại
        if (isPasswordVisible) {
            editText.inputType = android.text.InputType.TYPE_CLASS_TEXT
        } else {
            editText.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        // Di chuyển con trỏ đến cuối chuỗi
        editText.setSelection(editText.text.length)
    }

/// kiem tra noi dung nhap
    private fun kiem_tra_noi_dung_nhap_Login(editText: EditText): Boolean {

        if( editText.text.toString().length ==0 ){
            editText.error = "Nhập vào nội dung "
            return true
        }else{
            return false
        }
    }


    // code phần ghi nhớ đăng nhập
    override fun onPause() {
        super.onPause()
        ghinhodangnhap_themdulieu() // Thêm dữ liệu vào ShareRefrerence
    }

    override fun onResume() {
        super.onResume()
        ghinhodangnhap_laydulieu()// lấy dữ liệu từ ShareRefrerence
    }

    private fun ghinhodangnhap_laydulieu() {
        saveData = this.getSharedPreferences("data", Context.MODE_PRIVATE)
        // tạo 1 list để chưa tên và 1 list để chứa mật khẩu
        val arrayTenDangNhap = ArrayList<String>()
        val arrayMatKhau= ArrayList<String>()
        // lấy tên đăng nhập và mật khẩu
        val tendangnhap = saveData.getString("tendangnhap",null)
        val matkhau = saveData.getString("matkhau", null )
        // thêm tên vào list tên đăng nhập
        arrayTenDangNhap.add(tendangnhap.toString())
        // thêm mật khẩu vào list mật khẩu
        arrayMatKhau.add(matkhau.toString())


        // tạo 1 adapter chứa list tên đăng nhập rồi xét adapter đó cho Autotext nhập tên đăng nhập
        val adapterTenDangNhap = ArrayAdapter(this@ScreenHome, android.R.layout.simple_expandable_list_item_1,arrayTenDangNhap)
        edtInputUserName.setAdapter(adapterTenDangNhap)

        // tạo 1 adapter chứa list mật khẩu rồi xét adapter đó cho Autotext nhập mật khẩu
        val adapterMatKhau = ArrayAdapter(this@ScreenHome, android.R.layout.simple_expandable_list_item_1,arrayMatKhau)
        edtInputPassword.setAdapter(adapterMatKhau)




    }

    @SuppressLint("CommitPrefEdits")
    private fun ghinhodangnhap_themdulieu() {
        if(checkBoxRememberLogin.isChecked) {
            saveData = this.getSharedPreferences("data", Context.MODE_PRIVATE)
            val putData = saveData.edit()
            putData.putString("tendangnhap", edtInputUserName.text.toString())
            putData.putString("matkhau", edtInputPassword.text.toString())
            putData.apply()
        }
    }
}