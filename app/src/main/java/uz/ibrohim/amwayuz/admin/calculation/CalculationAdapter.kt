package uz.ibrohim.amwayuz.admin.calculation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.ibrohim.amwayuz.admin.products.ProductsItem
import uz.ibrohim.amwayuz.databinding.ItemCalculationDialogBinding

class CalculationAdapter(
    private val list: MutableList<ProductsItem>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CalculationAdapter.Vh>() {
    private var filteredList: MutableList<ProductsItem> = list

    inner class Vh(private val itemAdapterItemBinding: ItemCalculationDialogBinding) :
        RecyclerView.ViewHolder(itemAdapterItemBinding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun onBind(student: ProductsItem) {
            itemAdapterItemBinding.apply {

                calculationDialogName.text = student.name
                calculationDialogArtikul.text = student.code

                itemView.setOnClickListener {
                    listener.onItemClick(student, position)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCalculationDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(filteredList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(student: ProductsItem, position: Int)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}