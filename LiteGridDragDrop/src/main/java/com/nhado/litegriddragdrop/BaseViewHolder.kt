package com.nhado.litegriddragdrop

import android.view.View

abstract class BaseViewHolder(var itemView: View) {
    abstract fun onBindMode(mode : ModeBindView)

    enum class ModeBindView{
        DRAG, ENTERED, EXITED
    }
}