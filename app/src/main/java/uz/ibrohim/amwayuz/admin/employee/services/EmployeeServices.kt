package uz.ibrohim.amwayuz.admin.employee.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.ibrohim.amwayuz.admin.employee.request_response.RequestEmployee
import uz.ibrohim.amwayuz.admin.employee.request_response.ResponseEmployee

interface EmployeeServices {

    @POST("delete-user")
    suspend fun deleteUser(@Body request: RequestEmployee): Response<ResponseEmployee>

}