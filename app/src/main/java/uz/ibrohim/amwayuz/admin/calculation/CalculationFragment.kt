package uz.ibrohim.amwayuz.admin.calculation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.admin.home.ProductViewModel
import uz.ibrohim.amwayuz.admin.products.ProductsAdapter
import uz.ibrohim.amwayuz.admin.products.ProductsItem
import uz.ibrohim.amwayuz.databinding.DialogCalculationBinding
import uz.ibrohim.amwayuz.databinding.FragmentCalculationBinding
import uz.ibrohim.amwayuz.utils.Preferences
import java.util.Locale

class CalculationFragment : Fragment() {

    private var _binding: FragmentCalculationBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()

    private lateinit var productsList: ArrayList<ProductsItem>
    private lateinit var productsListFilter: ArrayList<ProductsItem>
    private lateinit var adapter: CalculationAdapter

    private lateinit var selectedItems: ArrayList<ProductsItem>
    private lateinit var selectedAdapter: CalculationsAdapter
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculationBinding.inflate(inflater, container, false)

        binding.apply {
            productsList = arrayListOf()
            productsListFilter = arrayListOf()
            selectedItems = arrayListOf()

            // Setup adapter for main RecyclerView (employee_rv)
            selectedAdapter = CalculationsAdapter(selectedItems, object : CalculationsAdapter.OnItemClickListener {
                override fun onItemClick(student: ProductsItem, position: Int) {
                    if (position in selectedItems.indices) {
                        selectedItems.removeAt(position)
                        selectedAdapter.notifyItemRemoved(position)
                        updateTotalPrice() // narxni qayta hisoblaymiz
                    }
                }
            })
            employeeRv.adapter = selectedAdapter

            // Open dialog
            calculationBasket.setOnClickListener {
                getDialog()
            }

            // Clear selected items
            calculationClear.setOnClickListener {
                selectedItems.clear()
                selectedAdapter.notifyDataSetChanged()
                totalPriceTxt.text = "0"
            }

            calculationCheck.setOnClickListener {
                if (selectedItems.isNotEmpty()){
                    val total = totalPriceTxt.text.toString()
                    val intent = Intent(requireContext(), PrintActivity::class.java)
                    intent.putExtra("items", ArrayList(selectedItems))
                    intent.putExtra("total", total)
                    startActivity(intent)
                }
            }
        }
        return binding.root
    }

    private fun getDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding: DialogCalculationBinding = DialogCalculationBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window!!.setLayout(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.show()

        dialogBinding.apply {
            productsList.clear()
            productsListFilter.clear()

            val selected = viewModel.selectedEmployee

            selected!!.forEach {
                productsList.add(it)
            }

            productsListFilter.addAll(productsList)

            adapter = CalculationAdapter(productsListFilter, object : CalculationAdapter.OnItemClickListener {
                override fun onItemClick(product: ProductsItem, position: Int) {
                    // Add selected product to selectedItems list
                    selectedItems.add(product)
                    selectedAdapter.notifyItemInserted(selectedItems.size - 1)
                    updateTotalPrice() // narxni qayta hisoblaymiz
                    dialog.dismiss()
                }
            })
            calculationRv.adapter = adapter

            calculationSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean = false

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(p0: String): Boolean {
                    val searchText = p0.lowercase(Locale.getDefault())
                    productsListFilter.clear()

                    if (searchText.isNotEmpty()) {
                        productsList.forEach {
                            if (it.name?.lowercase(Locale.getDefault())?.contains(searchText) == true ||
                                it.code?.lowercase(Locale.getDefault())?.contains(searchText) == true) {
                                productsListFilter.add(it)
                            }
                        }
                    } else {
                        productsListFilter.addAll(productsList)
                    }
                    calculationRv.adapter?.notifyDataSetChanged()
                    return true
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        val totalPrice = selectedItems.sumOf {
            it.price
                ?.replace(".", "") // nuqtani olib tashlaymiz
                ?.toDoubleOrNull() ?: 0.0
        }
        binding.totalPriceTxt.text = "Жами: ${"%,.0f".format(totalPrice)} сўм"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}