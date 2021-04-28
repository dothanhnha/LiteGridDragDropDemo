package com.nhado.litegriddragdrop

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout


class GridDragDrop(context: Context?, attrs: AttributeSet?) : GridLayout(context, attrs) {

    lateinit var adapter: GridDragDropAdapter<BaseViewHolder>

    fun registerAdapter(value: GridDragDropAdapter<*>){
        adapter = value as GridDragDropAdapter<BaseViewHolder>
    }

    fun bindView(){
        for (i in 0 until adapter.getItemCount()){
            var view = adapter.onCreateViewHolder(this@GridDragDrop, 0)
            adapter.onBindViewHolder(view,i)
            this@GridDragDrop.addView(view.itemView)
        }
    }
}