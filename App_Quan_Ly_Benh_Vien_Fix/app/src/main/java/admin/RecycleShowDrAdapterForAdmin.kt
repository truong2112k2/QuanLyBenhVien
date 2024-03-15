package admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ReturnThis
import androidx.recyclerview.widget.RecyclerView
import com.example.app_quan_ly_benh_vien_fix.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview.view.txtshowTitle
import kotlinx.android.synthetic.main.item_recycleview_manager_doctor.view.showDrAge
import kotlinx.android.synthetic.main.item_recycleview_manager_doctor.view.showDrDepartment
import kotlinx.android.synthetic.main.item_recycleview_manager_doctor.view.showDrImage
import kotlinx.android.synthetic.main.item_recycleview_manager_doctor.view.showDrName
import kotlinx.android.synthetic.main.item_recycleview_manager_doctor.view.showTypeOfDelease

class RecycleShowDrAdapterForAdmin(var list: List<DataDoctor>, val setOnClick: setOnClick): RecyclerView.Adapter<RecycleShowDrAdapterForAdmin.design>() {
    inner class design(view: View): RecyclerView.ViewHolder(view)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): design {
          val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview_manager_doctor,
              parent,
              false)
            return design(layout)

    }

    override fun onBindViewHolder(holder: design, position: Int) {
        holder.itemView.apply {
            showDrName.text = list[position].drName
            showDrAge.text = list[position].drAge
            showDrDepartment.text = list[position].medicalDepartment
            showTypeOfDelease.text = list[position].typeOfDesease
            Picasso.get().load(list[position].imageDr).into(showDrImage)
        }

        /// set sự kiện
        holder.itemView.setOnClickListener {
            setOnClick.setClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: List<DataDoctor>){
        list = searchList
        notifyDataSetChanged()
    }
}