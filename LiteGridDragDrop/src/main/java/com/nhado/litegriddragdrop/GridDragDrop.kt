package com.nhado.litegriddragdrop

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.drapdropcustom.MDragShadow
import kotlin.collections.ArrayList


class GridDragDrop(context: Context?, attrs: AttributeSet?) : GridLayout(context, attrs), View.OnLongClickListener {

    lateinit var adapter: GridDragDropAdapter<BaseViewHolder>

    fun registerAdapter(value: GridDragDropAdapter<*>) {
        adapter = value as GridDragDropAdapter<BaseViewHolder>
    }

    var listView: ArrayList<BaseViewHolder> = ArrayList()

    fun bindView() {
        (this@GridDragDrop as ViewGroup).removeAllViews()
        for (i in 0 until adapter.getItemCount()) {
            var view = adapter.onCreateViewHolder(this@GridDragDrop)
            adapter.onBindViewHolder(view, i)
            var viewType = adapter.getItemViewType(i)

            // wrap itemview by WrapperView have a field : "position"
            var viewWithWrapper: View
            if (view.itemView.parent !is Wrapper) {
                viewWithWrapper = genWrapper(viewType, i)
                viewWithWrapper.addView(view.itemView)
            } else {
                (view.itemView.parent as Wrapper).position = i
                viewWithWrapper = view.itemView.parent as Wrapper
            }
            view.itemView.setOnDragListener(genDragListener())
            view.itemView.setOnLongClickListener(this)
            listView.add(view)
            this@GridDragDrop.addView(viewWithWrapper)
        }
    }

    private fun getPositionFromItemView(view: View): Int? {
        if (view.parent is Wrapper)
            return (view.parent as Wrapper).position
        else
            return null
    }

    private fun genWrapper(viewType: ViewType, position: Int): Wrapper {
        val paramHolder = LayoutParams()
        paramHolder.height = ViewGroup.LayoutParams.MATCH_PARENT
        paramHolder.width = ViewGroup.LayoutParams.MATCH_PARENT
        paramHolder.rowSpec = viewType.rowSpec
        paramHolder.columnSpec = viewType.columnSpec
        paramHolder.setGravity(viewType.gravity)
        var wrapper = Wrapper(context, position)
        wrapper.layoutParams = paramHolder
        wrapper.tag = position
        return wrapper
    }

    private fun genDragListener(): OnDragListener {
        var dragListener = object : OnDragListener {

            override fun onDrag(view: View, event: DragEvent): Boolean {
                // Defines a variable to store the action type for the incoming event
                val action = event.action
                when (action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        return event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    }
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        return true
                    }
                    DragEvent.ACTION_DRAG_LOCATION -> {
                        return true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        return true
                    }
                    DragEvent.ACTION_DROP -> {
                        swapView(event, view)
                        return true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        val position = getPositionFromItemView(event.localState as View)
                        listView[position!!].itemView.isVisible = true
                        if (position != null)
                            adapter.onBindViewHolderForDragMode(listView[position!!], position)
                        return true
                    }
                    else -> Log.e(
                            "DragDrop Example",
                            "Unknown action type received by OnDragListener."
                    )
                }
                return false
            }
        }
        return dragListener
    }

    override fun onLongClick(view: View?): Boolean {

        listView.forEachIndexed { index, view ->
            adapter.onBindViewHolderForDragMode(view, index)
        }
        view?.isInvisible = true
        val item = ClipData.Item((view?.tag).toString() as CharSequence)
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(view?.tag.toString(), mimeTypes, item)

        if (view == null)
            return true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view?.startDragAndDrop(
                    data
                    , MDragShadow(view)
                    , view
                    , DRAG_FLAG_OPAQUE
            )
        } else {
            view?.startDrag(
                    data
                    , MDragShadow(view)
                    , view
                    , DRAG_FLAG_OPAQUE
            )
        }
        return true
    }

    fun swapView(event: DragEvent, view: View){
        val fromView = event.localState as View
        val fromWrapper = fromView.parent as Wrapper
        val toWrapper =
                view.parent as Wrapper //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
        val toView = toWrapper.get(0)

        val fromPosition = fromWrapper.position
        fromWrapper.position = toWrapper.position
        toWrapper.position = fromPosition

        fromWrapper.removeAllViews()
        toWrapper.removeAllViews()
        fromWrapper.addView(toView)
        fromView.isVisible = true
        toWrapper.addView(fromView)
    }

}