package com.nhado.litegriddragdrop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class GridDragDropAdapter<T : BaseViewHolder> {
    var listView: ArrayList<T> = ArrayList<T>()
    var grid:GridDragDrop? = null

    abstract fun onBindViewHolder(holder: T, position: Int)
    abstract fun onCreateViewHolder(parent: ViewGroup): T
    abstract fun getItemCount(): Int
    abstract fun getItemViewType(position: Int): ViewType
    fun notifiDatasetChange(mode: BaseViewHolder.ModeBindView?) {
        if (mode != null)
            for (i in 0 until getItemCount()) {
                listView[i].onBindMode(mode)
            }
        else
            grid?.bindView()
    }
    abstract fun onSwapDataset(fromPosition: Int, toPosition: Int)
}