package com.example.app_quan_ly_benh_vien_fix

import admin.DataDoctor
import admin.setOnClick
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycle_doctor_user.view.imgShowImageUser
import kotlinx.android.synthetic.main.item_recycle_doctor_user.view.txtShowNameDr_User
import kotlinx.android.synthetic.main.item_recycle_doctor_user.view.txtShowTypeDr_User

class RecycleShowDrUser(var list: List<DataDoctor>, val setOnClick: setOnClick): RecyclerView.Adapter<RecycleShowDrUser.design>() {
    inner class design(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): design {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_doctor_user, parent, false)
        return design(layout)
    }



    override fun onBindViewHolder(holder: design, position: Int) {
        holder.itemView.apply {
            txtShowNameDr_User.text = list[position].drName
            txtShowTypeDr_User.text= list[position].typeOfDesease
            Picasso.get().load(list[position].imageDr).into(imgShowImageUser)
        }
        holder.itemView.setOnClickListener {
            setOnClick.setClick(position)
        }
    }
    override fun getItemCount(): Int {
             return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun changeList(newList: List<DataDoctor> ){
        list = newList
        notifyDataSetChanged()
    }
}