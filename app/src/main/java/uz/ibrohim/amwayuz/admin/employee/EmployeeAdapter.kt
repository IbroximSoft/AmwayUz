package uz.ibrohim.amwayuz.admin.employee

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appuj.customizedalertdialoglib.CustomizedAlertDialog
import com.appuj.customizedalertdialoglib.CustomizedAlertDialogCallback
import uz.ibrohim.amwayuz.databinding.UsersItemBinding

class EmployeeAdapter(private val list: List<EmployeeItem>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<EmployeeAdapter.Vh>() {

    inner class Vh(private val itemAdapterItemBinding: UsersItemBinding) :
        RecyclerView.ViewHolder(itemAdapterItemBinding.root) {

        fun onBind(student: EmployeeItem) {
            itemAdapterItemBinding.usersTxt.text = student.name

            itemAdapterItemBinding.usersDelete.setOnClickListener {
                CustomizedAlertDialog.callAlertDialog(itemView.context, "Diqqat!",
                    "Siz ushbu kategoriyani o'chirmoqchimisiz ?",
                    "Xa", "Yo'q", false,
                    object : CustomizedAlertDialogCallback<String> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun alertDialogCallback(callback: String) {
                            if (callback == "1") {
                                listener.onItemClick(student, position)
                            }
                        }
                    })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(UsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    interface OnItemClickListener {
        fun onItemClick(student: EmployeeItem, position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}