package com.gm_demo.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gm_demo.MainActivity
import com.gm_demo.R
import com.gm_demo.base.FragmentBase
import com.gm_demo.bind.GenericRecyclerViewAdapter
import com.gm_demo.databinding.HomeFragmentBinding
import com.gm_demo.databinding.RowHomeBinding
import com.gm_demo.network.client.ResponseHandler
import com.gm_demo.network.model.ResponseListData
import com.gm_demo.sidemenu.DrawerAdapter
import com.gm_demo.sidemenu.SideMenuClickHandler
import com.gm_demo.sidemenu.SideMenuResponse
import com.gm_demo.utils.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : FragmentBase<HomeViewModel, HomeFragmentBinding>() {

    private var lastPosition: Int = 0

    private lateinit var viewmodel: HomeViewModel
    private var sideMenuList: ArrayList<SideMenuResponse.Genre> = ArrayList()
    private var trackItemList: ArrayList<HomeTrackResponse.Track> = ArrayList()
    var isCallApi = false
    var isTrackApiCalled = false
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var drawerAdapter: DrawerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.callGenereAPI()
        retainInstance = true
        mediaPlayer = MediaPlayer()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun setupToolbar() {
    }

    override fun initializeScreenVariables() {
        drawerAdapter = this.context?.let { it1 ->
            DrawerAdapter(
                it1,
                sideMenuList
            )
        }!!
        getDataBinding().rvSubList.adapter = drawerAdapter
        setTrackAdapter()
        setLiveDataObservers()
    }

    override fun getViewModel(): HomeViewModel? {
        viewmodel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return viewmodel
    }

    override fun unAuthorizationUser(message: String?, messageCode: String?) {
        message?.let { showSnackbar(it) }
    }

    private fun setLiveDataObservers() {

        viewmodel.responseHome.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    viewmodel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewmodel.showProgressBar(false)
                    isCallApi = false
                    httpFailedHandler(it.code, it.message, it.messageCode)

                }
                is ResponseHandler.OnSuccessResponse<ResponseListData<SideMenuResponse.Genre>?> -> {
                    viewmodel.showProgressBar(false)

                    isCallApi = false
                    if (it.response != null) {
                        it.response.data?.let { it1 -> sideMenuList.addAll(it1) }
                        (activity as MainActivity).setDrawerLayout(sideMenuList)
                        setDrawerListener()

                        drawerAdapter.setList(
                            sideMenuList
                        )
                    }
                }
            }
        })

        viewmodel.responseTrackHome.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    viewmodel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewmodel.showProgressBar(false)
                    isTrackApiCalled = false
                    httpFailedHandler(it.code, it.message, it.messageCode)

                }
                is ResponseHandler.OnSuccessResponse<ResponseListData<HomeTrackResponse.Track>?> -> {
                    viewmodel.showProgressBar(false)
                    isTrackApiCalled = false
                    trackItemList.clear()
                    if (it.response != null) {
                        it.response.data?.let { it1 -> trackItemList.addAll(it1) }
                        getDataBinding().rvList.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
    }


    private fun setTrackAdapter() {
        val myAdapter = object :
            GenericRecyclerViewAdapter<HomeTrackResponse.Track, RowHomeBinding>(
                context!!,
                trackItemList
            ) {
            override val layoutResId: Int
                get() = R.layout.row_home

            @SuppressLint("SetTextI18n")
            override fun onBindData(
                model: HomeTrackResponse.Track,
                position: Int,
                dataBinding: RowHomeBinding
            ) {
                dataBinding.executePendingBindings()
                dataBinding.txtTitle.text = model.name

                if (model.isPlaying) {
                    dataBinding.imgPlay.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_pause)
                    )
                } else {
                    dataBinding.imgPlay.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_play_arrow)
                    )
                }
            }

            override fun onItemClick(
                model: HomeTrackResponse.Track,
                position: Int,
                dataBinding: RowHomeBinding
            ) {
                if (!model.isPlaying) {
                    trackItemList[lastPosition].isPlaying = false
                    notifyItemChanged(lastPosition)

                    lastPosition = position
                    playsong(model.previewURL)
                    model.isPlaying = true
                    notifyItemChanged(position)
                } else {
                    model.isPlaying = false
                    notifyItemChanged(position)
                    pauseSong()
                }
            }

        }
        getDataBinding().rvList.adapter = myAdapter
    }

    private fun setDrawerListener() {
        (activity as MainActivity).drawerListner(object : SideMenuClickHandler {
            override fun clickOnMainCategory(id: String, name: String) {
                (activity as MainActivity).sideMenuOpen()

                GlobalScope.launch(context = Dispatchers.Main) {
                    delay(500)
                    lastPosition = 0
                    mediaPlayer.reset()
                    viewmodel.callTrackAPI(id)
                }

            }
        })
    }

    private fun setListner(clickHandler: SideMenuClickHandler) {
        drawerAdapter.setClickListener(clickHandler)
        getDataBinding().clickListener = clickHandler
    }

    private fun setRecyclerSideMenuListner(isApiCalling: Boolean) {

        setListner(object : SideMenuClickHandler {
            override fun clickOnMainCategory(id: String, name: String) {
                if (isApiCalling) {
                    GlobalScope.launch(context = Dispatchers.Main) {
                        delay(500)
                        lastPosition = 0
                        mediaPlayer.reset()
                        viewmodel.callTrackAPI(id)
                    }
                }
            }
        })
    }

    private fun playsong(url: String) {
        mediaPlayer.reset()

        mediaPlayer.apply {
            setDataSource(url)
            prepare()
            start()
        }

        mediaPlayer.setOnCompletionListener {

        }
    }

    private fun pauseSong() {
        mediaPlayer.stop()
    }

    override fun onStop() {
        mediaPlayer.release()
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getDataBinding().rvSubList.visibility = View.VISIBLE
            drawerAdapter.notifyDataSetChanged()
            drawerAdapter.setPosition((activity as MainActivity).drawerAdapter.lastPosition)
            (activity as MainActivity).manageDrawer(false)
            setRecyclerSideMenuListner(true)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRecyclerSideMenuListner(false)
            getDataBinding().rvSubList.visibility = View.GONE
            (activity as MainActivity).manageDrawer(true)
            (activity as MainActivity).drawerAdapter.setPosition(drawerAdapter.lastPosition)
            (activity as MainActivity).drawerAdapter.notifyDataSetChanged()
        }
    }
}




