package uz.ibrohim.amwayuz.admin.products

import java.io.Serializable

data class ProductsItem(
    var name: String? = null,
    var code: String? = null,
    var bodyPrice: String? = null,
    var price: String? = null,
    var amount: String? = null,
    var image: String? = null,
    var key: String? = null,
): Serializable
