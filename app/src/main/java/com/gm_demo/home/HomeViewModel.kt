package com.gm_demo.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gm_demo.base.ViewModelBase
import com.gm_demo.network.ApiClient
import com.gm_demo.network.client.ResponseHandler
import com.gm_demo.network.model.ResponseData
import com.gm_demo.network.model.ResponseListData
import com.gm_demo.sidemenu.SideMenuResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModelBase() {
    // TODO: Implement the ViewModel
    internal var responseHome =
        MutableLiveData<ResponseHandler<ResponseListData<SideMenuResponse.Genre>?>>()
    internal var responseTrackHome =
        MutableLiveData<ResponseHandler<ResponseListData<HomeTrackResponse.Track>?>>()
    private var homeRepository: HomeRepository? =
        HomeRepository(ApiClient.getApiInterface())

    fun callGenereAPI() {
        viewModelScope.launch(coroutineContext) {
            responseHome.value = ResponseHandler.Loading
            responseHome.value =
                homeRepository?.callGenereAPI()
        }
    }

    fun callTrackAPI(id: String) {
        viewModelScope.launch(coroutineContext) {
            responseTrackHome.value = ResponseHandler.Loading
            responseTrackHome.value =
                homeRepository?.callTrackAPI(id)
        }
    }
}
