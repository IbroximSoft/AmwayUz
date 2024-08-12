package uz.ibrohim.amwayuz.admin.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.databinding.FragmentProductsBinding
import uz.ibrohim.amwayuz.utils.Preferences
import java.util.Locale

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()
    private lateinit var productsList: ArrayList<ProductsItem>
    private lateinit var productsListFilter: ArrayList<ProductsItem>
    private lateinit var adapter: ProductsAdapter
    private val viewModel: ProductsModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        Preferences.init(requireContext())
        val isAdmin: String = Preferences.isAdmin

        if (!isAdmin.toBoolean()){
            binding.productsAdd.isVisible = false
        }

        productsList = arrayListOf()
        productsListFilter = arrayListOf()
        dataGet()

        binding.apply {
            productsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(p0: String): Boolean {

                    productsListFilter.clear()
                    val searchText = p0.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()){
                        productsList.forEach{
                            if (it.name?.lowercase(Locale.getDefault())!!.contains(searchText) ||
                                it.code?.lowercase(Locale.getDefault())!!.contains(searchText)){
                                productsListFilter.add(it)
                            }
                        }
                        productsRv.adapter?.notifyDataSetChanged()
                    }else{
                        productsListFilter.clear()
                        productsListFilter.addAll(productsList)
                        productsRv.adapter?.notifyDataSetChanged()
                    }
                    return true
                }

            })

            productsAdd.setOnClickListener {
                viewModel.bool = false
                findNavController().navigate(R.id.productsAddFragment)
            }
        }

        return binding.root
    }

    private fun dataGet() {
        db.collection("products").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val items: ProductsItem? = data.toObject(ProductsItem::class.java)
                        if (items != null) {
                            productsList.add(items)
                        }
                    }

                    productsListFilter.addAll(productsList)

                    adapter = ProductsAdapter(productsListFilter, object : ProductsAdapter.OnItemClickListener {
                        override fun onItemClick(student: ProductsItem, position: Int, status: String) {
                            val isAdmin: String = Preferences.isAdmin
                            if (isAdmin.toBoolean()){
                                viewModel.name = student.name.toString()
                                viewModel.code = student.code.toString()
                                viewModel.bodyPrice = student.bodyPrice.toString()
                                viewModel.price = student.price.toString()
                                viewModel.amount = student.amount.toString()
                                viewModel.image = student.image.toString()
                                viewModel.key = student.key.toString()
                                viewModel.bool = true
                                findNavController().navigate(R.id.productsAddFragment)
                            }
                        }

                    })
                    binding.productsRv.adapter = adapter
                }
            }
    }
}