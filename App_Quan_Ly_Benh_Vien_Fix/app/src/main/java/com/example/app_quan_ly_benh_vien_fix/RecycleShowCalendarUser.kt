package com.example.app_quan_ly_benh_vien_fix

import admin.setOnClick
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_screen_home.view.textView
import kotlinx.android.synthetic.main.item_recycleview_viewcalendarbook_user.view.imgDrScreenShowCalendar
import kotlinx.android.synthetic.main.item_recycleview_viewcalendarbook_user.view.txtShowDateScreenShowCalendar
import kotlinx.android.synthetic.main.item_recycleview_viewcalendarbook_user.view.txtShowNameDrScreenShowCalendar
import kotlinx.android.synthetic.main.item_recycleview_viewcalendarbook_user.view.txtShowTimeScreenShowCalendar

class RecycleShowCalendarUser(var list: List<DataBook>, val click: setOnClick): RecyclerView.Adapter<RecycleShowCalendarUser.design>() {
    inner class design(view:View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): design {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview_viewcalendarbook_user, parent, false )
        return design(view)
    }

    override fun onBindViewHolder(holder: design, position: Int) {
       holder.itemView.apply {
           txtShowNameDrScreenShowCalendar.text = list[position].doctor!!.drName
           txtShowDateScreenShowCalendar.text = list[position].date
           txtShowTimeScreenShowCalendar.text = list[position].time
           Picasso.get().load(list[position].doctor!!.imageDr).into(imgDrScreenShowCalendar)
       }
        holder.itemView.setOnClickListener {
            click.setClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun changeList(newList: List<DataBook>){
        list = newList
    }
}
