package com.example.app_quan_ly_benh_vien_fix

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_add_account.btnRegisterAccount
import kotlinx.android.synthetic.main.activity_screen_add_account.btnShow_Password1
import kotlinx.android.synthetic.main.activity_screen_add_account.btnShow_Password2
import kotlinx.android.synthetic.main.activity_screen_add_account.edtInputRegisterEmail
import kotlinx.android.synthetic.main.activity_screen_add_account.edtInputRegisterPassWord
import kotlinx.android.synthetic.main.activity_screen_add_account.edtInputRegisterPassWordSecond
import kotlinx.android.synthetic.main.activity_screen_add_account.edtInputRegisterUserName
import java.util.regex.Pattern

class ScreenAddAccount : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_add_account)
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC")
        khoi_tao_nut_dang_ky()
        thuc_hien_bat_tat_mat_khau()
    }

    private fun thuc_hien_bat_tat_mat_khau() {
        val ani = AnimationUtils.loadAnimation(this@ScreenAddAccount, R.anim.animation)
        btnShow_Password1.setOnClickListener {
            bat_tat_mat_khau(edtInputRegisterPassWord)
            btnShow_Password1.startAnimation(ani)
        }
        btnShow_Password2.setOnClickListener {
            bat_tat_mat_khau(edtInputRegisterPassWordSecond)
            btnShow_Password2.startAnimation(ani)

        }
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



    private fun khoi_tao_nut_dang_ky() {
        btnRegisterAccount.setOnClickListener {
            if(kiem_tra_noi_dung_nhap(edtInputRegisterUserName)
                || kiem_tra_noi_dung_nhap(edtInputRegisterPassWord)
                || kiem_tra_noi_dung_nhap(edtInputRegisterPassWordSecond)
                || kiem_tra_noi_dung_nhap(edtInputRegisterEmail)){
                // kiểm tra xem nội dung có bị bỏ trống hay không
            }else if(
                kiem_Tra_Dieu_Kien_Nhap(edtInputRegisterUserName)
                || kiem_Tra_Dieu_Kien_Nhap(edtInputRegisterPassWord)
                || kiem_Tra_Dieu_Kien_Nhap(edtInputRegisterPassWordSecond)
                || kiem_Tra_Dieu_Kien_Nhap(edtInputRegisterEmail)){
                // kiểm tra xem tên và mật khẩu có đủ có số và chữ không

            } else if(xu_ly_mat_khau(edtInputRegisterPassWord, edtInputRegisterPassWordSecond)){
                // kiểm tra xem 2 mật khẩu ở 2 lần nhập có trùng nhau không
            }else if(kiem_tra_Email(edtInputRegisterEmail)){
                edtInputRegisterEmail.error ="Sai định dạng Email"
            }
            else {

                db.addListenerForSingleValueEvent( object : ValueEventListener{
                    // kiểm tra xem tài khoản tồn tại trên firebase chưa
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for( i in snapshot.children ){
                            val data = i.getValue(DataAccount :: class.java)
                            if( data != null && data.name == edtInputRegisterUserName.text.toString() && data.password == edtInputRegisterPassWord.text.toString()){
                                // nếu tồn tại rồi thì t hiển thị ra 1 dialog thông báo
                                khoitaoAlertThongBao("Tài khoản đã tồn tại !")
                                return
                            }
                        }
                        themAccount()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ScreenAddAccount, "Không thể kết nối đến Firebase", Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
    }



    /// tạo Aler để thông báo
    private fun khoitaoAlertThongBao( thongbao : String) {
        val alertDialog = AlertDialog.Builder(this@ScreenAddAccount)
        alertDialog.apply {
            setTitle(thongbao)
            setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
        }
        }.show()

    }

    // hàm thêm Account
    private fun themAccount() {
        val name = edtInputRegisterUserName.text.toString()
                val pass = edtInputRegisterPassWord.text.toString()
                val email = edtInputRegisterEmail.text.toString()
                val id = db.push().key!!
                val dataPut = DataAccount(id, name, pass, email)
                db.child(id).setValue(dataPut)
                    .addOnSuccessListener {
                        khoitaoAlertThongBao("Đăng ký thành công")
                        edtInputRegisterUserName.text.clear()
                        edtInputRegisterPassWord.text.clear()
                        edtInputRegisterPassWordSecond.text.clear()
                        edtInputRegisterEmail.text.clear()
                    }
                    .addOnFailureListener {
                        khoitaoAlertThongBao("Đăng ký thất bại")
                    }
    }

   /// hàm kiểm tra xem 2 lần nhập mật khẩu có trùng nhau không
    private fun xu_ly_mat_khau(text: EditText, text2: EditText): Boolean {
        val password = text.text.toString()
        val password2 = text2.text.toString()

        if(password.equals(password2)){
            return false
        }else{
            Toast.makeText(this@ScreenAddAccount, "Mật khẩu không trùng khớp !", Toast.LENGTH_SHORT).show()
            return true
        }
    }
  /// hàm kiểm tra xem tên đăng nhập và mật khẩu có chứa cả số và chữ hay không
    private fun kiem_Tra_Dieu_Kien_Nhap(editText: EditText): Boolean {
        var demso = 0
        var demchu = 0
        val list = editText.text.toString().toList()

        for( i in list){
            if(i.isDigit()){
                demso++
            }else if( i.isLetter()){
                demchu++
            }
        }
        if( demso > 0 && demchu > 0){

            return false
        }else {
            editText.error = "Nội dung cần gồm chữ và số"
            return true
        }

    }
/// Hàm kiểm tra xem nội dung có bị bỏ trống không
    private fun kiem_tra_noi_dung_nhap(editText: EditText): Boolean {

        if( editText.text.toString().length ==0 ){
            editText.error = "Nhập vào nội dung "
            return true
        }else{
            return false
        }
    }

    // hàm kiểm tra định dạng email
    private fun kiem_tra_Email(editText: EditText): Boolean {
        val email = editText.text.toString()
        // Biểu thức chính quy kiểm tra định dạng email
        val form = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        // tạo 1 biến để biên dịch một biểu thức chính quy trong biến form thành một đối tượng có thể sử dụng để kiểm tra chuỗi.
        val testEmail = Pattern.compile(form)
        // tạo 1 biến sử dụng biểu thức chính quy để kiểm tra xem một chuỗi có khớp với biểu thức đó hay không.
        // so sánh email xem có giống kiểu nhập với testEmail không bằng biểu thức Matcher
        val doTest = testEmail.matcher(email)

        if(doTest.matches()){ // nếu hàm này trả về true -> nghĩa là định dạng của email giống với định dạng mà t quy định tại testEmail
            return false

        }else{
            return true
        }
    }
}