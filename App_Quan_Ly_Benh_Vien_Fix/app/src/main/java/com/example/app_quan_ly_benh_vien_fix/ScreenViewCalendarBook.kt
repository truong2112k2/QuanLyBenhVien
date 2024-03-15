package com.example.app_quan_ly_benh_vien_fix

import admin.DataDoctor
import admin.setOnClick
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view_calendar_book.recycleShowCalendarBookForUser
import kotlinx.android.synthetic.main.activity_view_calendar_book.txtNumberItem
import java.text.SimpleDateFormat
import java.util.Locale

class ScreenViewCalendarBook : AppCompatActivity() {
    private lateinit var db : DatabaseReference
    private lateinit var list : ArrayList<DataBook>
    private lateinit var alerdialogUpdateAndDelete : AlertDialog
    private lateinit var adt : RecycleShowCalendarUser
    private var countClicktimes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_calendar_book)
        val id = intent.getStringExtra("idnguoidung2")
        db = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(id.toString()).child("DATA_BOOK")
        list = ArrayList<DataBook>()

        show_data_calendar()

        recycleShowCalendarBookForUser.layoutManager = LinearLayoutManager(this@ScreenViewCalendarBook)

    }




    private fun show_data_calendar() {
        db.addListenerForSingleValueEvent( object : ValueEventListener{

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                    for (i in snapshot.children) {
                        val data = i.getValue(DataBook::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    if (list.isEmpty()) {
                        recycleShowCalendarBookForUser.visibility = View.GONE
                        txtNumberItem.visibility = View.VISIBLE
                    } else {
                        recycleShowCalendarBookForUser.visibility = View.VISIBLE
                        txtNumberItem.visibility = View.GONE
                        adt = RecycleShowCalendarUser(list, object : setOnClick {
                            @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
                            override fun setClick(postition: Int) {
                                val idBook = list[postition].idBook

                                if (kiem_tra_thoi_han(list[postition].date.toString(), idBook!!)) {

                                } else {
                                    val patientId = list[postition].idBook
                                    val patientName = list[postition].patientName
                                    val patientNumber = list[postition].patientNumber
                                    val patientGmail = list[postition].patientGmail
                                    val patientDate = list[postition].date
                                    val patientTime = list[postition].time
                                    val nodeDr: DataDoctor? = list[postition].doctor
                                    xoaVaSuaLichKham(
                                        patientId,
                                        patientName,
                                        patientNumber,
                                        patientGmail,
                                        patientDate,
                                        patientTime,
                                        nodeDr
                                    )
                                }

                            }
                        })

                        recycleShowCalendarBookForUser.adapter = adt
                    }
                }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ScreenViewCalendarBook, "Không thể kết nối đến Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("InflateParams", "SetTextI18n", "MissingInflatedId", "SuspiciousIndentation")
    private fun xoaVaSuaLichKham(idBook: String?, name: String?, phone: String?, gmail: String?, date: String?, time: String?, nodeDr: DataDoctor? ) {
        val idUser = intent.getStringExtra("idnguoidung2") // id acc người dùng
        val idNodeBook  = idBook.toString()
        val builder = AlertDialog.Builder(this@ScreenViewCalendarBook)
        val view = layoutInflater.inflate(R.layout.alerdialog_update_delete_book, null )
        // tương tác view tại đaây
        val linearTable = view.findViewById<LinearLayout>(R.id.tableUpdateInfo)
        val btnStartUpdate = view.findViewById<Button>(R.id.btnStartUpdate)
        val txtDoYouWant = view.findViewById<TextView>(R.id.txtDoYouWant)
        val edtNamePatient = view.findViewById<EditText>(R.id.edtInPutPatientNameUpdate)
        val edtNumberPatient = view.findViewById<EditText>(R.id.edtInPutPatientPhoneUpdate)
        val edtGmailPatient = view.findViewById<EditText>(R.id.edtInputPatientGmailUpdate)
        val txtshowDate = view.findViewById<TextView>(R.id.txtShowDateBookUpdate)
        val txtshowTime = view.findViewById<TextView>(R.id.txtShowTimeBookUpdate)
        val btnPickDate = view.findViewById<Button>(R.id.btnPickDateBookUpdate)
        val btnPickTime = view.findViewById<Button>(R.id.btnPickTimeBookUpdate)
        val btnDeleteAndComfimUpdate = view.findViewById<Button>(R.id.btnDeleteBookAndConfirmUpdate)
        // xét giá trị mặc định
        edtNamePatient.setText(name.toString())
        edtNumberPatient.setText(phone.toString())
        edtGmailPatient.setText(gmail.toString())
        txtshowDate.setText(date.toString())
        txtshowTime.setText(time.toString())
        btnPickDate.setOnClickListener {
          val calendar1 = Calendar.getInstance()
            calendar1.add( Calendar.MONTH, -1 )
            val timeStart = calendar1.timeInMillis
            val calendar2 = Calendar.getInstance()
            calendar2.add(Calendar.YEAR, 20)
            val timeEnd = calendar2.timeInMillis
            val build1 = CalendarConstraints.Builder()
            build1.setStart(timeStart)
            build1.setEnd(timeStart)
            val build2 = build1.build()
            val datepicker = MaterialDatePicker.Builder.datePicker()
            datepicker.setTitleText("Chọn ngày")
            val bornDatePicker  = datepicker.build()
            bornDatePicker.show(supportFragmentManager, "date")
            bornDatePicker.addOnPositiveButtonClickListener {
                val timepick = it.toLong()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timepick
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                val showDate = "$day/$month/$year"
                txtshowDate.text = showDate
            }
        }
        btnPickTime.setOnClickListener {
            TimePickerDialog(this@ScreenViewCalendarBook, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    txtshowTime.text = "$i:$i2"

            },0,0,true).show()
        }
        btnStartUpdate.setOnClickListener {
            countClicktimes++
            if( countClicktimes % 2 == 0){
                linearTable.visibility = View.GONE
                txtDoYouWant.visibility = View.VISIBLE
                btnStartUpdate.setText("Cập nhật")
                btnDeleteAndComfimUpdate.setText("Xóa lịch hẹn")
                // code xóa lịch hẹn
//                btnDeleteAndComfimUpdate.setOnClickListener {
//                    val dbRemove = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(idUser.toString()).child("DATA_BOOK").child(idNodeBook.toString())
//                     val reMove = dbRemove.removeValue()
//                         . addOnSuccessListener {
//                             val alertRemove = AlertDialog.Builder(this@ScreenViewCalendarBook)
//                             alertRemove.apply {
//                                 alertRemove.setTitle("Xóa thành công")
//                                 setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
//                                         dialogInterface.dismiss()
//                                     // clear thông tin
//                                     /// đóng dialog
//                                 }
//                             }
//                                 .show()
//                         }
//                         .addOnFailureListener {
//                             val alertRemove = AlertDialog.Builder(this@ScreenViewCalendarBook)
//                             alertRemove.apply {
//                                 alertRemove.setTitle("Xóa thất bại")
//                                 setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
//                                     dialogInterface.dismiss()
//                                 }
//                             }
//                                 .show()
//                         }
//                }
            }else {
                linearTable.visibility = View.VISIBLE
                txtDoYouWant.visibility = View.GONE
                btnStartUpdate.setText("Hủy")
                btnDeleteAndComfimUpdate.setText("Xác nhận")
                // cập nhật thông tin
//                btnDeleteAndComfimUpdate.setOnClickListener {
//                    val dbUpdate = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(idUser.toString()).child("DATA_BOOK").child(idNodeBook.toString())
//                    val id_update = idNodeBook.toString()
//                    val name_patient_update =  edtNamePatient.text.toString()
//                    val phone_patient_update = edtNumberPatient.text.toString()
//                    val gmail_patient_update = edtGmailPatient.text.toString()
//                    val date_update = txtshowDate.text.toString()
//                    val time_update = txtshowTime.text.toString()
//                    val doctorDate: DataDoctor? = nodeDr
//
//                    val nodeBookUpdate = DataBook(id_update,
//                        name_patient_update,
//                        phone_patient_update,
//                        gmail_patient_update,
//                        date_update,
//                        time_update,
//                        doctorDate)
//                    dbUpdate.setValue(nodeBookUpdate)
//                        .addOnSuccessListener {
//                            Toast.makeText(this@ScreenViewCalendarBook, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
//                            alerdialogUpdateAndDelete.dismiss()
//                            recreate()
//                        }
//                        .addOnFailureListener {
//                            Toast.makeText(this@ScreenViewCalendarBook, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
//                            alerdialogUpdateAndDelete.dismiss()
//
//                        }
//
//                }
            }
        }
        builder.setView(view)
        alerdialogUpdateAndDelete = builder.create()
        alerdialogUpdateAndDelete.show()



        btnDeleteAndComfimUpdate.setOnClickListener {
            if(btnDeleteAndComfimUpdate.text == "Xóa lịch hẹn"){
                    val dbRemove = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(idUser.toString()).child("DATA_BOOK").child(idNodeBook.toString())
                     val reMove = dbRemove.removeValue()
                         . addOnSuccessListener {
                             val alertRemove = AlertDialog.Builder(this@ScreenViewCalendarBook)
                             alertRemove.apply {
                                 alertRemove.setTitle("Xóa thành công")
                                 setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                         dialogInterface.dismiss()
                                         alerdialogUpdateAndDelete.dismiss()
                                     recreate()
                                 }
                             }
                                 .show()
                         }
                         .addOnFailureListener {
                             val alertRemove = AlertDialog.Builder(this@ScreenViewCalendarBook)
                             alertRemove.apply {
                                 alertRemove.setTitle("Xóa thất bại")
                                 setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->
                                     dialogInterface.dismiss()
                                 }
                             }
                                 .show()
                         }

            }else if(btnDeleteAndComfimUpdate.text == "Xác nhận" ){
                // cập nhật thông tin
                    val dbUpdate = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(idUser.toString()).child("DATA_BOOK").child(idNodeBook.toString())
                    val id_update = idNodeBook.toString()
                    val name_patient_update =  edtNamePatient.text.toString()
                    val phone_patient_update = edtNumberPatient.text.toString()
                    val gmail_patient_update = edtGmailPatient.text.toString()
                    val date_update = txtshowDate.text.toString()
                    val time_update = txtshowTime.text.toString()
                    val doctorDate: DataDoctor? = nodeDr

                    val nodeBookUpdate = DataBook(id_update,
                        name_patient_update,
                        phone_patient_update,
                        gmail_patient_update,
                        date_update,
                        time_update,
                        doctorDate)
                    dbUpdate.setValue(nodeBookUpdate)
                        .addOnSuccessListener {
                            Toast.makeText(this@ScreenViewCalendarBook, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                            alerdialogUpdateAndDelete.dismiss()
                            recreate()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@ScreenViewCalendarBook, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                            alerdialogUpdateAndDelete.dismiss()

                        }
//

            }
        }


    }

    override fun recreate() { // hàm tự động reload activity
        super.recreate()
    }

    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
    private fun kiem_tra_thoi_han(string: String, idNode: String): Boolean { // kiểm tra xem ngày khám đã hết hạn chưa
        val dateInput = string.toString()
        val dateSystem = Calendar.getInstance().time

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    ///   val formatDateInput = dateFormat.format(dateInput)
        val formatDateSystem = dateFormat.format(dateSystem)

        if(dateInput.compareTo(formatDateSystem) < 0){
           basicAlertDialog("Lịch hẹn đã hết hạn, bạn có muốn xóa ?", idNode)
            return true
        }else if(dateInput.compareTo(formatDateSystem) == 0){
            Toast.makeText(this, "Hôm nay hết hạn", Toast.LENGTH_SHORT).show()
            return true
        }
        return false





//        val timeNow : Calendar = Calendar.getInstance()
//
//        val dateFormat = SimpleDateFormat("dd/MM/yy")
//        val time2 = dateFormat.parse(string)
//
//        if( time2 != null ) {
//            val newDate = Calendar.getInstance()
//            newDate.time = time2
//            if(newDate.equals(timeNow)){
//                Toast.makeText(this@ScreenViewCalendarBook, "Chú ý,lịch hết hạn vào hôm nay", Toast.LENGTH_SHORT).show()
//
//            }else if (newDate.before(timeNow)) {
//                basicAlertDialog("Lịch hẹn đã hết hạn, bạn có muốn xóa ?", idNode)
//                return true
//            }
//        }
//            return false
    }

    private fun basicAlertDialog(string: String , idNodeBook: String) {
        val idUser = intent.getStringExtra("idnguoidung2") // id acc người dùng
        val alertDialog = AlertDialog.Builder(this@ScreenViewCalendarBook)

        alertDialog.apply {
            setTitle(string)
            setPositiveButton("[Ok]"){ dialogInterface: DialogInterface, i: Int ->

                val dbremove = FirebaseDatabase.getInstance().getReference("DATA_ACC").child(idUser.toString()).child("DATA_BOOK").child(idNodeBook.toString())
                val remove = dbremove.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this@ScreenViewCalendarBook, "Xóa lịch thành công", Toast.LENGTH_SHORT).show()
                        recreate()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this@ScreenViewCalendarBook, "Không thể xóa", Toast.LENGTH_SHORT).show()
                    }


            }
            setNegativeButton("[Bỏ qua]"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        }.show()

    }

}