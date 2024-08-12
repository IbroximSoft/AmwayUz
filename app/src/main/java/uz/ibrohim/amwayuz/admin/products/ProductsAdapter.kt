package uz.ibrohim.amwayuz.admin.products

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.databinding.ProductsItemBinding

class ProductsAdapter(
    private val list: MutableList<ProductsItem>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductsAdapter.Vh>() {
    private var filteredList: MutableList<ProductsItem> = list
    private var status = ""

    inner class Vh(private val itemAdapterItemBinding: ProductsItemBinding) :
        RecyclerView.ViewHolder(itemAdapterItemBinding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        fun onBind(student: ProductsItem) {
            itemAdapterItemBinding.apply {
                student.apply {
                    itemView.context.apply {
                        productsItemName.text = getString(R.string.product_name, name)
                        productsCode.text = getString(R.string.artikul, code)
                        productsBodyPrice.text = getString(R.string.body_price, bodyPrice)
                        productsPrice.text = getString(R.string.price, price)
                        productsAmount.text = getString(R.string.amount, amount)
                        productsItemName.setSelected(true)
                        productsCode.setSelected(true)
                        productsBodyPrice.setSelected(true)
                        productsPrice.setSelected(true)
                        productsAmount.setSelected(true)
                    }
                }
                Glide.with(itemView.context).load(student.image).into(image)

                itemView.setOnClickListener {
                    status = "edite"
                    listener.onItemClick(student, position, status)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ProductsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(filteredList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(student: ProductsItem, position: Int, status: String)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}