package uz.ibrohim.amwayuz.admin.calculation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.ibrohim.amwayuz.admin.products.ProductsItem
import uz.ibrohim.amwayuz.databinding.ItemCalculationBinding

class CalculationsAdapter(
    private val list: MutableList<ProductsItem>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CalculationsAdapter.Vh>() {
    private var filteredList: MutableList<ProductsItem> = list

    inner class Vh(private val itemAdapterItemBinding: ItemCalculationBinding) :
        RecyclerView.ViewHolder(itemAdapterItemBinding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged", "SetTextI18n")
        fun onBind(student: ProductsItem) {
            itemAdapterItemBinding.apply {

                calculationName.text = student.name
                calculationArtikul.text = student.code
                calculationPrice.text = "${student.price} so'm"
                Glide.with(itemView.context).load(student.image).into(calculationImage)

                calculationDelete.setOnClickListener {
                    listener.onItemClick(student, position)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCalculationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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