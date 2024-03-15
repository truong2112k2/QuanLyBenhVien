package admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_quan_ly_benh_vien_fix.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycleview_department.view.imgShowImageDepart
import kotlinx.android.synthetic.main.item_recycleview_department.view.txtShowAge_Depart
import kotlinx.android.synthetic.main.item_recycleview_department.view.txtShowName_Depart


class RecycleShowDrInDepartment(val list: List<DataDepartment>): RecyclerView.Adapter<RecycleShowDrInDepartment.design>() {
    inner class design(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): design {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview_department, parent, false)
        return design(layout)
    }

    override fun onBindViewHolder(holder: design, position: Int) {
       holder.itemView.apply {
           txtShowName_Depart.text = list[position].node?.drName.toString()
           txtShowAge_Depart.text = list[position].node?.drAge.toString()
           Picasso.get().load(list[position].node?.imageDr).into(imgShowImageDepart)
       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}