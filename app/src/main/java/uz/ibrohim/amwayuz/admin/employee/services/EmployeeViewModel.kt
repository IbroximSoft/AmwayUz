package uz.ibrohim.amwayuz.admin.employee.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.ibrohim.amwayuz.admin.employee.request_response.RequestEmployee
import uz.ibrohim.amwayuz.retrofit.NetworkHelper
import uz.ibrohim.amwayuz.retrofit.Resource

class EmployeeViewModel(apiServices: EmployeeServices, private val networkHelper: NetworkHelper) : ViewModel() {

    private val moveRepository = EmployeeRepository(apiServices)
    val flow = MutableStateFlow<Resource>(Resource.Loading)

    fun deleteUser(requestEmployee: RequestEmployee): StateFlow<Resource> {
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkConnected()) {
                    val response = moveRepository.deleteUser(requestEmployee = requestEmployee)
                    if (response.isSuccessful && response.body() != null) {
                        flow.emit(Resource.Success(response.body()!!))
                    } else {
                        flow.emit(Resource.Error("Sever Error !"))
                    }
                } else {
                    flow.emit(Resource.Error("Network no connection !"))
                }
            } catch (e: Exception) {
                flow.emit(Resource.Error(e.message.toString()))
            }
        }
        return flow
    }
}