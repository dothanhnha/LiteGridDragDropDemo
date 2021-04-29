package com.nhado.litegriddragdrop

import android.view.Gravity
import android.widget.GridLayout
import android.widget.GridLayout.Spec
import android.widget.GridLayout.UNDEFINED

class ViewType {
    var isEmpty : Boolean = false
    var rowSpec: GridLayout.Spec = GridLayout.spec(UNDEFINED)
    var columnSpec: GridLayout.Spec = GridLayout.spec(UNDEFINED)
    var gravity: Int = Gravity.CENTER

    class Builder{
        private var viewType : ViewType = ViewType()

        fun setEmpty(isEmpty : Boolean) : Builder{
            viewType.isEmpty = isEmpty
            return this
        }

        fun setRowSpec(rowSpec: GridLayout.Spec) : Builder{
            viewType.rowSpec = rowSpec
            return this
        }

        fun setColumnSpec(columnSpec: GridLayout.Spec) : Builder{
            viewType.columnSpec = columnSpec
            return this
        }

        fun setGravity(gravity: Int) : Builder{
            viewType.gravity = gravity
            return this
        }

        fun build(): ViewType{
            return viewType
        }
    }
}