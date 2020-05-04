package com.gm_demo.network.client

import com.gm_demo.home.HomeTrackResponse
import com.gm_demo.network.model.ResponseData
import com.gm_demo.network.model.ResponseListData
import com.gm_demo.sidemenu.SideMenuResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("genres?")
    suspend fun callGenereAPI(
    ): Response<ResponseListData<SideMenuResponse.Genre>>


    @GET("genres/{id}/tracks/top?")
    suspend fun callTrackAPI(
        @Path("id") value: String
    ): Response<ResponseListData<HomeTrackResponse.Track>>

}