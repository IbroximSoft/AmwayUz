package uz.ibrohim.amwayuz.admin.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.amwayuz.admin.products.ProductsItem
import uz.ibrohim.amwayuz.databinding.FragmentHomeBinding
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()
    private lateinit var productsList: ArrayList<ProductsItem>
    private lateinit var productsListFilter: ArrayList<ProductsItem>
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeSearch.queryHint = "Izlash..."

        productsList = arrayListOf()
        productsListFilter = arrayListOf()
        dataGet()

        binding.apply {
            homeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(p0: String): Boolean {

                    productsListFilter.clear()
                    val searchText = p0.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()) {
                        productsList.forEach {
                            if (it.name?.lowercase(Locale.getDefault())!!.contains(searchText) ||
                                it.code?.lowercase(Locale.getDefault())!!.contains(searchText)
                            ) {
                                productsListFilter.add(it)
                            }
                        }
                        homeRv.adapter?.notifyDataSetChanged()
                        nullDataList()
                    } else {
                        productsListFilter.clear()
                        productsListFilter.addAll(productsList)
                        homeRv.adapter?.notifyDataSetChanged()
                        nullDataList()
                    }
                    return true
                }

            })
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
                    adapter = HomeAdapter(productsListFilter)
                    binding.homeRv.adapter = adapter
                }
                nullDataList()
            }
    }

    private fun nullDataList() {
        binding.apply {
            if (productsListFilter.isNotEmpty()) {
                homeNoData.isVisible = false
                homeRv.isVisible = true
            } else {
                homeRv.isVisible = false
                homeNoData.isVisible = true
            }
        }
    }
}