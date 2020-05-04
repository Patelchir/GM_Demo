package com.gm_demo.home

import com.gm_demo.base.BaseRepository
import com.gm_demo.network.client.ApiInterface
import com.gm_demo.network.client.ResponseHandler
import com.gm_demo.network.model.ResponseListData
import com.gm_demo.sidemenu.SideMenuResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(private val apiInterface: ApiInterface) : BaseRepository() {

    suspend fun callGenereAPI(): ResponseHandler<ResponseListData<SideMenuResponse.Genre>?> {
        return withContext(Dispatchers.Default) {
            return@withContext makeAPICall(
                call = {
                    apiInterface.callGenereAPI()
                })
        }
    }

    suspend fun callTrackAPI(id: String): ResponseHandler<ResponseListData<HomeTrackResponse.Track>?> {
        return withContext(Dispatchers.Default) {
            return@withContext makeAPICall(
                call = {

                    apiInterface.callTrackAPI(
                        id
                    )
                })
        }
    }
}