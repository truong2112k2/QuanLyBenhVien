package com.example.app_quan_ly_benh_vien_fix

import admin.DataDoctor

class DataBook(
    val idBook :String? = null,
    val patientName: String? = null,
    val patientNumber: String? = null,
    val patientGmail: String? = null,
    val date : String? = null ,
    val time: String? = null,
    val doctor: DataDoctor? = null,

) {
}