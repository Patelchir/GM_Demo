package com.gm_demo.sidemenu

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gm_demo.R


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DrawerAdapter(
    private val context: Context,
    private var rowModels: ArrayList<SideMenuResponse.Genre>
) : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    private var clickHandler: SideMenuClickHandler? = null
    var lastPosition: Int = 0

    fun setClickListener(clickHandler: SideMenuClickHandler) {
        this.clickHandler = clickHandler
    }

    fun setList(rowModels: ArrayList<SideMenuResponse.Genre>) {
        this.rowModels = rowModels

        notifyDataSetChanged()
    }

    fun setPosition(position: Int) {
        lastPosition = position
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var txtNavigationItem: TextView =
            itemView.findViewById(R.id.txtNavigationItem) as TextView

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item_drawer_layout,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val row = rowModels[position]


        viewHolder.txtNavigationItem.text = row.name

        if (row.isActive)
            viewHolder.itemView.background =
                ContextCompat.getDrawable(context, R.color.ripple_color)
        else
            viewHolder.itemView.background =
                ContextCompat.getDrawable(context, R.color.transparent)

        viewHolder.itemView.setOnClickListener {
            if (!rowModels[position].isActive) {
                rowModels[lastPosition].isActive = false
                notifyItemChanged(lastPosition)
                rowModels[position].isActive = true
                notifyItemChanged(position)
                lastPosition = position
                clickHandler?.clickOnMainCategory(row.id, row.name)
            }

        }
    }

    override fun getItemCount() = rowModels!!.size


}
