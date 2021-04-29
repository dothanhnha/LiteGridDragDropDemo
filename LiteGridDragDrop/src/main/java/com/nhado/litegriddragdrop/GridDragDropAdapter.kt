package com.nhado.litegriddragdrop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class GridDragDropAdapter<T : BaseViewHolder?> {

    var draggingPosition : Int? = null

    abstract fun onCreateViewHolder(parent: ViewGroup): T
    abstract fun getItemCount(): Int
    abstract fun onBindViewHolder(holder: T, position: Int)
    abstract fun onBindViewHolderForDragMode(holder: T, position: Int)
    abstract fun getItemViewType(position: Int): ViewType
}