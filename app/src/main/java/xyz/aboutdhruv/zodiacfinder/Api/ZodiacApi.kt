package xyz.aboutdhruv.zodiacfinder.Api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import xyz.aboutdhruv.zodiacfinder.Modal.UserModal

interface ZodiacApi {

//    @POST("/v1/astro_details")
//    fun getZodiacDetails(@Body userDetail: UserModal) : Call<UserModal>

    @POST("/v1/astro_details")
    suspend fun getZodiacDetails(@Body requestBody: RequestBody): Response<ResponseBody>


}