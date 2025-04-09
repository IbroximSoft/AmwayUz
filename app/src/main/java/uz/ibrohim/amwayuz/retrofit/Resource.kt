package uz.ibrohim.amwayuz.retrofit

sealed class Resource {

    data object Loading : Resource()
    data class Success(val data: Any) : Resource()
    data class Error(val message: String) : Resource()
}