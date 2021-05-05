package com.nhado.litegriddragdrop

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout

class Wrapper(context: Context, var position: Int) : ConstraintLayout(context) {

    init {
        clipChildren = false
    }
}