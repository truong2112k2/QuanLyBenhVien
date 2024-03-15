package com.example.app_quan_ly_benh_vien_fix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycleview.view.showImage
import kotlinx.android.synthetic.main.item_recycleview.view.txtshowTitle

class adapterRecycleView (val list: List<DataRecycleView>): RecyclerView.Adapter<adapterRecycleView.design>() {
    inner class design(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): design {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview, parent, false)
        return  design(layout)
    }

    override fun onBindViewHolder(holder: design, position: Int) {
        holder.itemView.apply {
            showImage.setImageResource(list[position].image)
            txtshowTitle.text = list[position].title
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
}