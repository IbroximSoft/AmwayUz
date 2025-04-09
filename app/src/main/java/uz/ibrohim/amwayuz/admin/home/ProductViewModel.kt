package uz.ibrohim.amwayuz.admin.home

import androidx.lifecycle.ViewModel
import uz.ibrohim.amwayuz.admin.products.ProductsItem

class ProductViewModel : ViewModel() {
    var selectedEmployee: ArrayList<ProductsItem>? = null
}