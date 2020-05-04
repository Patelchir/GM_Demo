package com.gm_demo

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.gm_demo.base.ActivityBase
import com.gm_demo.base.ViewModelBase
import com.gm_demo.databinding.ActivityMainBinding
import com.gm_demo.home.HomeFragment
import com.gm_demo.sidemenu.DrawerAdapter
import com.gm_demo.sidemenu.SideMenuClickHandler
import com.gm_demo.sidemenu.SideMenuResponse
import com.gm_demo.utils.Utils
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class MainActivity : ActivityBase<ViewModelBase>() {

    private var dialog: Dialog? = null
    lateinit var binding: ActivityMainBinding
    var toolbar: ConstraintLayout? = null
    private lateinit var viewModelBase: ViewModelBase
    lateinit var drawerAdapter: DrawerAdapter
    lateinit var sideMenuList: ArrayList<SideMenuResponse.Genre>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModelBase = ViewModelProvider(this).get(ViewModelBase::class.java)

        toolbar = binding.layToolbar.toolbar


        supportFragmentManager.inTransaction { add(R.id.containerView, HomeFragment()) }


        toolbar!!.sideMenu.setOnClickListener {
            sideMenuOpen()
        }

    }

    fun displayProgress(t: Boolean) {
        // binding.loading = t
        if (t) {
            if (dialog == null)
                dialog = Utils.progressDialog(this)

            dialog?.show()
        } else {
            dialog?.cancel()
        }
    }


    fun drawerListner(clickHandler: SideMenuClickHandler) {
        drawerAdapter.setClickListener(clickHandler)
        binding.clickListener = clickHandler
    }

    fun sideMenuOpen() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun setDrawerLayout(sideMenuList: ArrayList<SideMenuResponse.Genre>) {
        drawerAdapter = DrawerAdapter(this, sideMenuList)
        binding.customDrawerList.rvList.adapter = drawerAdapter
        this.sideMenuList = sideMenuList
        drawerAdapter.setList(sideMenuList)

    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun manageDrawer(isVisible: Boolean) {
        if (isVisible) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            binding.layToolbar.sideMenu.visibility = View.VISIBLE
        } else {
            binding.layToolbar.sideMenu.visibility = View.GONE
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}
