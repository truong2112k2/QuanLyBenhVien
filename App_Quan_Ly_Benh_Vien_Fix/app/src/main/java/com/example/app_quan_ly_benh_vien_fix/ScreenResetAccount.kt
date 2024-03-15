package com.example.app_quan_ly_benh_vien_fix

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_screen_reset_account.btnConfirmReset
import kotlinx.android.synthetic.main.activity_screen_reset_account.checkboxImNotRobot
import kotlinx.android.synthetic.main.activity_screen_reset_account.edtInputResetForNameUser
import kotlinx.android.synthetic.main.activity_screen_reset_account.edtInputResetForEmail
import kotlinx.android.synthetic.main.activity_screen_reset_account.txtresetPassfollowUser
import kotlinx.android.synthetic.main.activity_screen_reset_account.txtShowNewPassAfterReset
import kotlin.random.Random

class ScreenResetAccount : AppCompatActivity() {
    private lateinit var db : DatabaseReference
    private lateinit var alertChangePass : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_reset_account)
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC")
        khoi_tao_nut_lay_ma()
        khoi_tao_nut_doi_lai_mat_khau()
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun khoi_tao_nut_doi_lai_mat_khau() {
       val a = "Đổi lại mật khẩu"
        val span = SpannableString(a)
        span.setSpan(UnderlineSpan(),0,16,1)
        txtresetPassfollowUser.text = span

        txtresetPassfollowUser.setOnClickListener {

            val ani = AnimationUtils.loadAnimation(this@ScreenResetAccount, R.anim.animation)
            txtresetPassfollowUser.startAnimation(ani)
            val build = AlertDialog.Builder(this@ScreenResetAccount)
            val view = layoutInflater.inflate(R.layout.alerdialog_change_pass, null )
            val name = view.findViewById<EditText>(R.id.edtInputUserName_changepass)
            val gmail = view.findViewById<EditText>(R.id.edtInputUserGmail_changepass)
            val code = view.findViewById<EditText>(R.id.edtGetCode_changepass)
            val newPass = view.findViewById<EditText>(R.id.edtInputNewPass_changepass)
            val btnConfirm = view.findViewById<Button>(R.id.btnConfirmChangePass2)

            name.setText(edtInputResetForNameUser.text.toString())
            gmail.setText(edtInputResetForEmail.text.toString())
            val dbRev  =  FirebaseDatabase.getInstance().getReference("DATA_ACC")
            btnConfirm.setOnClickListener {
                val alerdialog = AlertDialog.Builder(this@ScreenResetAccount)
                alerdialog.apply {
                    setTitle("Lưu ý!")
                    setMessage("Sau khi đặt lại mật khẩu các thông tin sẽ không còn")
                    setNegativeButton("[ Hủy ]"){ dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("[Xác nhận]"){ dialogInterface: DialogInterface, i: Int ->
                if( kiem_tra_noi_dung_nhap_reset(code) || kiem_tra_noi_dung_nhap_reset(newPass)){

                }else{
                    dbRev.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for( i in snapshot.children){
                                val data = i.getValue(DataAccount ::class.java)
                                if(data != null && data.name == name.text.toString() && data.email == gmail.text.toString() && data.password == code.text.toString()){
                                    val userId = data.id.toString()
                                    val userName = name.text.toString()
                                    val userGmail = gmail.text.toString()
                                    val useNewPass = newPass.text.toString()
                                    val dbUpdate = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(userId.toString())
                                    // tiếp theo t phải thêm 1 biến data chứa dữ liệu mà t muốn update
                                    val dataReset  = DataAccount( useNewPass)
                                    // cập nhật data lên node
                                    dbUpdate.setValue(dataReset)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@ScreenResetAccount, "Mật khẩu của bạn đã được đổi ♥", Toast.LENGTH_SHORT).show()
                                            alertChangePass.dismiss()
                                            edtInputResetForNameUser.text.clear()
                                            edtInputResetForEmail.text.clear()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this@ScreenResetAccount, "Mật khẩu của bạn chưa được thay đổi", Toast.LENGTH_SHORT).show()
                                        }

                                    return
                                }
                            }
                            Toast.makeText(this@ScreenResetAccount, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                    }
                }.show()
            }




            build.setView(view)
            alertChangePass = build.create()
            alertChangePass.show()

        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun khoi_tao_nut_lay_ma() {
        val animation = AnimationUtils.loadAnimation(this@ScreenResetAccount, R.anim.animation)
          btnConfirmReset.setOnClickListener {
              btnConfirmReset.startAnimation(animation)
              if (kiem_tra_noi_dung_nhap_reset(edtInputResetForNameUser) || kiem_tra_noi_dung_nhap_reset(edtInputResetForEmail)
              )else if( checkboxImNotRobot.isChecked == false) {
                  Toast.makeText(this@ScreenResetAccount, "Xác nhận bạn không phải máy", Toast.LENGTH_SHORT).show()
              } else {
                  db.addListenerForSingleValueEvent(object : ValueEventListener {
                      override fun onDataChange(snapshot: DataSnapshot) {
                          for (i in snapshot.children) {
                              // nếu tài khoản vừa nhập vào đã tồn tại trong firebase
                              // ta sẽ thực hiện cập nhật
                              val data = i.getValue(DataAccount::class.java)
                              if (data != null && data.name == edtInputResetForNameUser.text.toString() && data.email == edtInputResetForEmail.text.toString()) {
                                  val id:String = data.id.toString()
                                  val name:String = data.name.toString()
                                ///  val pass = data.password
                                  val email:String = data.email.toString()
                                // thực hiện reset tài khoản
                                  thuchienResetTaiKhoan(id,name,email)
                                  return
                              }
                          }
                          // nếu chưa tài khaorn vừa nhập vào chưa tồn tại trong firebase
                          // ta sẽ thông báo cho người dùng
                          val alertDialog = AlertDialog.Builder(this@ScreenResetAccount)
                          alertDialog.apply {
                              setTitle("Tài khoản của bạn chưa tồn tại")
                              setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                   dialogInterface.dismiss()
                              }
                          }.show()
                      }

                      override fun onCancelled(error: DatabaseError) {
                          Toast.makeText(
                              this@ScreenResetAccount,
                              "Không thể kết nối đến Firebase!",
                              Toast.LENGTH_SHORT
                          ).show()
                      }
                  })
              }
          }


    }




    @SuppressLint("SetTextI18n")
    private fun thuchienResetTaiKhoan(id_demo: String, name_demo:String, email_demo:String) {
        val id = id_demo.toString()
        val name  = name_demo.toString()
        val email = email_demo.toString()
        // tạo 1 mật khẩu mới bằng thư viện Random
        val new_pass = Random.nextInt(1000,9999)
        // sau đó t sẽ update mật khẩu mới này vào tài khoản theo id
        //đầu tiên t phải lấy ra node có id bằng id của tài khoản vừa login
        val dbrev = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(id.toString())
        // tiếp theo t phải thêm 1 biến data chứa dữ liệu mà t muốn update
        val dataReset  = DataAccount(id, name, new_pass.toString(), email )
        // cập nhật data lên node
        dbrev.setValue(dataReset)

        txtShowNewPassAfterReset.visibility = View.VISIBLE
        txtresetPassfollowUser.visibility = View.VISIBLE
        txtShowNewPassAfterReset.text = "Mật khẩu mới của bạn là: "+ dataReset.password


    }


    private fun kiem_tra_noi_dung_nhap_reset(editText: EditText): Boolean {

        if( editText.text.toString().length ==0 ){
            editText.error = "Nhập vào nội dung "
            return true
        }else{
            return false
        }
    }
}