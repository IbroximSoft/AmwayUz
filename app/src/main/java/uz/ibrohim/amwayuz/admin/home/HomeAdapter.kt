package uz.ibrohim.amwayuz.admin.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.admin.products.ProductsItem
import uz.ibrohim.amwayuz.databinding.HomeItemBinding

class HomeAdapter(
    list: MutableList<ProductsItem>
) :
    RecyclerView.Adapter<HomeAdapter.Vh>() {
    private var filteredList: MutableList<ProductsItem> = list

    inner class Vh(private val itemAdapterItemBinding: HomeItemBinding) :
        RecyclerView.ViewHolder(itemAdapterItemBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun onBind(student: ProductsItem) {
            itemAdapterItemBinding.apply {
                student.apply {
                    itemView.context.apply {
                        productsItemName.text = getString(R.string.product_name, name)
                        productsCode.text = getString(R.string.artikul, code)
                        productsPrice.text = getString(R.string.price, price)
                        productsAmount.text = getString(R.string.amount, amount)
                        productsItemName.setSelected(true)
                        productsCode.setSelected(true)
                        productsPrice.setSelected(true)
                        productsAmount.setSelected(true)
                    }
                }
                Glide.with(itemView.context).load(student.image).into(image)

                itemView.setOnClickListener {
//                    status = "amount"
//                    listener.onItemClick(student, position, status)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(filteredList[position])
    }

//    interface OnItemClickListener {
//        fun onItemClick(student: ProductsItem, position: Int, status: String)
//    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}