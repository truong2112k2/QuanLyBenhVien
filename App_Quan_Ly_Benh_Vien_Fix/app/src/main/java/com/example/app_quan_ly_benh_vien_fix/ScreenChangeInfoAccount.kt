package com.example.app_quan_ly_benh_vien_fix

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_change_info_account.btnConFirmChange
import kotlinx.android.synthetic.main.activity_screen_change_info_account.edtNewEmail
import kotlinx.android.synthetic.main.activity_screen_change_info_account.edtNewName
import kotlinx.android.synthetic.main.activity_screen_change_info_account.edtNewPass
import kotlinx.android.synthetic.main.activity_screen_change_info_account.edtOldName
import kotlinx.android.synthetic.main.activity_screen_change_info_account.edtOldPass
import java.util.regex.Pattern

class ScreenChangeInfoAccount : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_change_info_account)
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC")
        thay_doi_thong_tin()
    }

    private fun thay_doi_thong_tin() {
        btnConFirmChange.setOnClickListener { // khi t click vào nút xác nhận thay đổi
            val oldName = edtOldName.text.toString() // lấy ra tên đăng nhập vừa nhập vào
            val oldPass = edtOldPass.text.toString() // lấy ra mật khẩu vừa nhập vào
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                  for( i in snapshot.children){
                      val data = i.getValue(DataAccount :: class.java)
                      // thực hiện kiểm tra xem tài khoản đã tồn tại trong firebase chưa
                      // nếu tồn tại rồi -> ta sẽ cho phép người dùng thực hiện thay đổi
                      if( data != null && data.name == oldName && data.password == oldPass){
                          val name:String = data.name.toString()
                          val id:String = data.id.toString()
                          val email:String = data.email.toString()
                          val pass:String = data.password.toString()
                          thuc_hien_cap_nhat_thong_tin(id,name,pass,email)  // hàm thực hiện đổi thông tin
                          return
                      }
                  }
                    // nếu tài khoản không tồn tại, t sẽ cho hiện lên 1 alerdialog để thông báo
                    val alertDialog = AlertDialog.Builder(this@ScreenChangeInfoAccount)
                    alertDialog.apply {
                        setTitle("Tài khoản của bạn chưa tồn tại")
                        setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.dismiss()
                        }
                    }.show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ScreenChangeInfoAccount, "Không thể kết nối Firebase", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }

    private fun thuc_hien_cap_nhat_thong_tin(id_demo:String,name_demo :String, pass_demo:String, email_demo:String ) {
        val dbrev = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(id_demo.toString())
        val newID :String = id_demo
        var newName:String = name_demo
        var newPass:String = pass_demo
        var newEmail:String = email_demo
        if(edtNewName.text.toString().length ==0 && edtNewPass.text.toString().length ==0 && edtNewEmail.text.toString().length ==0){
            Toast.makeText(this@ScreenChangeInfoAccount, "Bạn chưa nhập vào bất cứ nội dung nào", Toast.LENGTH_SHORT).show()
        }else if(kiem_Tra_Dieu_Kien_Nhap(edtNewName) || kiem_Tra_Dieu_Kien_Nhap(edtNewPass)){

        } else if(kiem_tra_Email(edtNewEmail)){
             edtNewEmail.error = "Nhập sai định dạng Email"
        } else{
            if( edtNewName.text.toString().length > 0){
                newName = edtNewName.text.toString()
            }
            if( edtNewPass.text.toString().length >0){
                newPass = edtNewPass.text.toString()
            }
            if( edtNewEmail.text.toString().length >0){
                newEmail = edtNewEmail.text.toString()
            }
            val dataUpdate = DataAccount(newID, newName, newPass, newEmail)
            dbrev.setValue(dataUpdate)
                .addOnSuccessListener {
                    Toast.makeText(this@ScreenChangeInfoAccount, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@ScreenChangeInfoAccount, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()

                }
        }


    }
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


    // kiểm tra mật khẩu nhập vào có gồm số và chữ không
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


}