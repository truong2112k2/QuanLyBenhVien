package com.example.app_quan_ly_benh_vien_fix

import admin.DataDoctor
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_screen_book_dr.btnComfimBook
import kotlinx.android.synthetic.main.activity_screen_book_dr.btnPickDateBook
import kotlinx.android.synthetic.main.activity_screen_book_dr.btnPickTimeBook
import kotlinx.android.synthetic.main.activity_screen_book_dr.edtInPutPatientName
import kotlinx.android.synthetic.main.activity_screen_book_dr.edtInPutPatientPhone
import kotlinx.android.synthetic.main.activity_screen_book_dr.edtInputPatientGmail
import kotlinx.android.synthetic.main.activity_screen_book_dr.txtShowDateBook
import kotlinx.android.synthetic.main.activity_screen_book_dr.txtShowTimeBook
import java.util.regex.Pattern

class ScreenBookDr : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_book_dr)
        khoi_tao_lich_kham()


    }

    private fun khoi_tao_lich_kham() {
        khoi_tao_chon_ngay_va_thoi_gian()
        btnComfimBook.setOnClickListener {
            val getIdNguoiDUng = intent.getStringExtra("idnguoidung3")
            db = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(getIdNguoiDUng.toString()).child("DATA_BOOK")
            // lấy id người dùng, lưu ý: id của acc người dùng
            // ta phải lấy id của acc ng dùng để t thêm node book lịch vào đúng với từng tài khoản
            // ví dụ tài khoản A đặt bác sĩ B thì t sẽ thêm node book lịch chứa bác sĩ b vào tài khoản A thông qua id của nó
            // vì vậy t phải  lấy được id của tài khoản A
            val getDataDr = intent.getParcelableExtra<DataDoctor>("dataDr",)
            // lấy dữ liệu dr từ activity trước, để thêm dữ liệu của bác sĩ vào node book lịch
            // sau đó thêm node book lịch vào node account người dùng thông qua id đã lấy trên

            if( kiem_tra_nhap(edtInPutPatientName)
                || kiem_tra_nhap(edtInPutPatientPhone)
                || kiem_tra_nhap(edtInputPatientGmail)
                ){
            }else if(kiem_tra_Email(edtInputPatientGmail)){
                edtInputPatientGmail.error ="Email sai định dạng"
            }else if(kiem_tra_thoi_gian()){

            } else{
                if (getDataDr != null) { // nếu dữ liệu bác sĩ lấy ra không trống(!= null)
//                     chỗ này tạo 1 databook
//                     lấy tên, số điện thoại, email từ editext
//                     lấy ngày tháng khám từ textview
//                     lấy data bác sĩ từ intent màn hình trước
//                     sau đó tạo và thêm node databook vào node accountUser thông qua ID
//                     11:17AM 22/2/2024

                    val idBook = db.push().key!!
                    val patientName = edtInPutPatientName.text.toString()
                    val patientNumber = edtInPutPatientPhone.text.toString()
                    val patientGmail = edtInputPatientGmail.text.toString()
                    val dateBook = txtShowDateBook.text.toString()
                    val timeBook = txtShowTimeBook.text.toString()
                    val dataBook = DataBook(idBook,patientName,patientNumber,patientGmail,dateBook,timeBook,getDataDr)
                    db.child(idBook).setValue(dataBook)
                        .addOnSuccessListener {
                            val alertDialog = AlertDialog.Builder(this@ScreenBookDr)
                            alertDialog.apply {
                                setTitle("Đặt lịch thành công")
                                setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                     dialogInterface.dismiss()
                                    tro_ve_man_hinh_show_bac_si()
                                    clearThongTin()
                                }
                            }.show()
                        }

                        .addOnFailureListener {
                            val alertDialog2 = AlertDialog.Builder(this@ScreenBookDr)
                            alertDialog2.apply {
                                setTitle("Đặt lịch thất bại")
                                setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                    dialogInterface.dismiss()
                                }
                            }.show()
                        }
                }

            }
        }
    }

    private fun tro_ve_man_hinh_show_bac_si() {
        // node lại
        val i = Intent(this@ScreenBookDr, ScreenViewDr :: class.java)
        startActivity(i)
        finish()
        /*
        Hàm finish() thường được gọi sau khi chuyển đổi đến một Activity mới hoặc khi bạn muốn đóng một Activity hiện tại và quay lại Activity trước đó.
        Nói đơn giản giả sử bạn có 3 màn hình
        Nếu bạn muốn nhảy từ màn hình 3 về màn hình 1 mà k gọi onFinish()
        Thì khi bạn bấm nút quay lại, cho dù bạn đã dùng intent, android vẫn sẽ đưa bạn về màn hình 2
        tại vì màn hình 2 vẫn nằm trong ngăn xếp khi bạn chưa gọi hàm onFinish()
        Sau khi bạn gọi hàm onFinish, màn hình thứ 2 gần như sẽ bị hủy luôn
         */
    }

    private fun clearThongTin() {
        edtInPutPatientName.text.clear()
        edtInputPatientGmail.text.clear()
        edtInPutPatientPhone.text.clear()
        txtShowDateBook.setText(" ")
        txtShowTimeBook.setText(" ")
        edtInPutPatientName.requestFocus()
    }

    private fun kiem_tra_thoi_gian(): Boolean {
        if( txtShowDateBook.text.toString() == "00/00/0000" || txtShowTimeBook.text.toString() == "00:00"){
            Toast.makeText(this@ScreenBookDr, "Hãy chọn ngày và giờ", Toast.LENGTH_SHORT).show()
            return true
        }
        return false

    }

    @SuppressLint("SetTextI18n")
    private fun khoi_tao_chon_ngay_va_thoi_gian() {
        // khởi tạo hiệu ứng click
        val ani = AnimationUtils.loadAnimation(this@ScreenBookDr, R.anim.animation)
        // nhớ takenote lại date và timepicker
        btnPickDateBook.setOnClickListener { // khi click vào nút chọn ngày t sẽ tạo ra 1 hộp thoại chọn ngày
            btnPickDateBook.startAnimation(ani)
            val calendar: Calendar = Calendar.getInstance()
            // khởi tạo 1 biến theo kiểu Calendar để tương tác với thời gian
            calendar.add(Calendar.MONTH, -1 )
            /*
            Sử dụng phương thức add() của đối tượng Calendar để thay đổi thời gian hiện tại
            calendar.add(Calendar.MONTH, -1) giảm đi 1 tháng từ thời gian hiện tại.
             */
            val startDate: Long = calendar.timeInMillis
            // lấy thời gian trong đối tượng calendar và gán vào biến startDate
            val calendarEnd: Calendar = Calendar.getInstance()
            // khởi tạo 1 biến theo kiểu Calendar để tương tác với thời gian
            calendarEnd.add(Calendar.YEAR, 20)
            /*
            sử dụng phương thức add() của đối tượng Calendar để thay đổi thời gian hiện tại
            calendarEnd.add(Calendar.YEAR, 20) thêm 20 năm vào thời gian hiện tại
             */
            val endDate: Long = calendarEnd.timeInMillis
            // lấy thời gian trong đối tượng calendar và gán vào biến EndDate
            val constraintsBuilder = CalendarConstraints.Builder()
            // tạo một biến têm constraintsBuilder thuộc lớp CalendarConstraints cung cấp các phương thức để tùy chỉnh và xây dựng một CalendarConstraints theo ý muốn.
            constraintsBuilder.setStart(startDate) // gán thời gian bắt đầu vào
            constraintsBuilder.setEnd(endDate) // gán thời gian kết thúc vào
            val celendarConstraints = constraintsBuilder.build() // tạo một biến CelandarConstraints từ biến constraintsBuilder
            val builder = MaterialDatePicker.Builder.datePicker()
            // khởi tạo 1 đối tượng builder theo kiểu MaterialDatePicker
            builder.setTitleText("Chọn ngày") // thiết lập tiêu đề cho biến builder
            builder.setCalendarConstraints(celendarConstraints) // gán biến CalendarConstraints vừa tạo bên trên vào biến builder
            val datePicker = builder.build() // tạo một biến datePicker từ biến builder đã được thiết lập ở bên trên

            datePicker.show(supportFragmentManager,"datePicker") // hiển thị datepicker
              /*
      hiển thị MaterialDatePicker trên supportFragmentManager của activity hiện tại. "datePicker" là một nhãn định danh cho MaterialDatePicker,
            cho phép bạn xác định nó khi bạn cần tương tác với nó trong tương lai.
               */

            // lắng nghe sự kiện và sử lý ngày khi người dùng chọn
            datePicker.addOnPositiveButtonClickListener {
                val selectDate = it as Long
                // sử lý ngày đã chọn
                val calendarSelected = Calendar.getInstance()
                calendarSelected.timeInMillis = selectDate
                val day = calendarSelected.get(Calendar.DAY_OF_MONTH) //  lấy ngày
                val month = calendarSelected.get(Calendar.MONTH) + 1 // lấy tháng
                val year = calendarSelected.get(Calendar.YEAR) // lấy năm
                val showDate = "$day/$month/$year"
                txtShowDateBook.text = showDate
            }



        }
        btnPickTimeBook.setOnClickListener {// khi click vào nút chọn giờ t sẽ tạo ra 1 hộp thoại chọn giờ
            btnPickTimeBook.startAnimation(ani)
            TimePickerDialog(this@ScreenBookDr, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                // i1 là giờ, i2 là phút
             txtShowTimeBook.setText("$i:$i2")
            },0,0,true).show()
        }
    }

    private fun kiem_tra_nhap(editText: EditText): Boolean{
        if(editText.text.toString().length == 0){
            editText.error ="Hãy nhập nội dung"
            return true
        }
        return false
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
}