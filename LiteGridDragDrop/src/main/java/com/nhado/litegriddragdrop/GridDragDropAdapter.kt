package com.nhado.litegriddragdrop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class GridDragDropAdapter<T : BaseViewHolder?> {

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T
    abstract fun getItemCount(): Int

    abstract fun onBindViewHolder(holder: T, position: Int)
}