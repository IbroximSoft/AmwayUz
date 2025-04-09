package uz.ibrohim.amwayuz.retrofit

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ibrohim.amwayuz.App
import uz.ibrohim.amwayuz.admin.employee.services.EmployeeServices

object ApiClient {
    private const val base_url = "http://192.168.100.72:8080/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Response code test
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor(App.instance))
        .build()

    val deleteEmployee: EmployeeServices = getRetrofit().create(EmployeeServices::class.java)
}