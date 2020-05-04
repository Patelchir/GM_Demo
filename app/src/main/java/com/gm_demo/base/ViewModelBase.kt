package com.gm_demo.base

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


open class ViewModelBase : ViewModel() {
    private val job = SupervisorJob()
    val coroutineContext = Dispatchers.Main + job
    var snackbarMessage = MutableLiveData<Any>()
    var progressBarDisplay = MutableLiveData<Boolean>(false)
    var bottomSheetMenuDisplay = MutableLiveData<Boolean>(false)
    private var hideKeyBoardEvent = MutableLiveData<Any>()

    /**
     * Cancel the job when the view model is destroyed
     */
    override fun onCleared() {
        try {
            super.onCleared()
            coroutineContext.cancel()
        } catch (e: Exception) {
            Log.e("data", "data")
        }
    }

    fun getSnakeBarMessage(): MutableLiveData<Any> {
        return snackbarMessage
    }


    fun getProgressBar(): MutableLiveData<Boolean> {
        return progressBarDisplay
    }

    fun getbottomSheetMenu(): MutableLiveData<Boolean> {
        return bottomSheetMenuDisplay
    }

    fun displayBottomSheetMenu(boolean: Boolean) {
        bottomSheetMenuDisplay.value = boolean
    }


    fun getHideKeyBoardEvent(): MutableLiveData<Any> {
        return hideKeyBoardEvent
    }

    /**
     * This method is used to display the Snake Bar Message
     *
     * @param message string object to display.
     */
    fun showSnackbarMessage(message: Any) {
        snackbarMessage.value = message
    }


    fun showProgressBar(display: Boolean) {
        progressBarDisplay.value = display
    }




}