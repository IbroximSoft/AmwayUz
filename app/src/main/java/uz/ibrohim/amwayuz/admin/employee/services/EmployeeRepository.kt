package uz.ibrohim.amwayuz.admin.employee.services

import uz.ibrohim.amwayuz.admin.employee.request_response.RequestEmployee

class EmployeeRepository(private val apiServices: EmployeeServices) {

    suspend fun deleteUser(requestEmployee: RequestEmployee) = apiServices.deleteUser(request = requestEmployee)
}