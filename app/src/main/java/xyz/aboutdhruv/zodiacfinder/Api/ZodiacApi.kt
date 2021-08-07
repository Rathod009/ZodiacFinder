package xyz.aboutdhruv.zodiacfinder.Api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import xyz.aboutdhruv.zodiacfinder.Modal.UserModal

interface ZodiacApi {

    @POST("/v1/astro_details")
    fun getZodiacDetails(@Body userDetail: UserModal) : Call<UserModal>
}