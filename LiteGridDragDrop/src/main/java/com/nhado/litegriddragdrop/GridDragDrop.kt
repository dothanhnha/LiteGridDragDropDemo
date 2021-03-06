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
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.drapdropcustom.BaseDragShadow
import kotlin.collections.ArrayList


class GridDragDrop(context: Context?, attrs: AttributeSet?) : GridLayout(context, attrs), View.OnLongClickListener {

    lateinit var adapter: GridDragDropAdapter<BaseViewHolder>

    var listView: ArrayList<BaseViewHolder> = ArrayList()

    var dragShow: DragShadowBuilder? = null

    var buildDragShadow: ((view: View) -> DragShadowBuilder)? = null

    private fun genDragShadow(view: View): DragShadowBuilder =
        buildDragShadow?.invoke(view) ?: BaseDragShadow(view)


    fun registerAdapter(value: GridDragDropAdapter<*>) {
        adapter = value as GridDragDropAdapter<BaseViewHolder>
        adapter.callBackGridDatasetChange = {
            bindView()
        }
    }

    fun bindView() {
        (this@GridDragDrop as ViewGroup).removeAllViews()
        listView.clear()
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
                        var pairWrapper = getPairWrapper(event, view)
                        Log.d("dragStarted", pairWrapper.dragWrapper.position.toString() + "_" + pairWrapper.effectedWrapper.position)
                        return event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    }
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        var pairWrapper = getPairWrapper(event, view)
                        Log.d("dragEntered", pairWrapper.dragWrapper.position.toString() + "_" + pairWrapper.effectedWrapper.position)
                        listView[pairWrapper.effectedWrapper.position].onBindMode(BaseViewHolder.ModeBindView.ENTERED)
                        return true
                    }
                    DragEvent.ACTION_DRAG_LOCATION -> {
                        var pairWrapper = getPairWrapper(event, view)
                        Log.d("dragLocation", pairWrapper.dragWrapper.position.toString() + "_" + pairWrapper.effectedWrapper.position)
                        return true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        var pairWrapper = getPairWrapper(event, view)
                        Log.d("dragExited", pairWrapper.dragWrapper.position.toString() + "_" + pairWrapper.effectedWrapper.position)
                        listView[pairWrapper.effectedWrapper.position].onBindMode(BaseViewHolder.ModeBindView.EXITED)
                        return true
                    }
                    DragEvent.ACTION_DROP -> {
                        swapView(event, view)
                        return true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        var pairWrapper = getPairWrapper(event, view)
                        Log.d("dragEnded", pairWrapper.dragWrapper.position.toString() + "_" + pairWrapper.effectedWrapper.position)
                        listView[pairWrapper.dragWrapper.position].itemView.isVisible = true
                        return true
                    }
                    else -> Log.e(
                            "LiteGridDragDropDemo",
                            "Unknown action type received by OnDragListener."
                    )
                }
                return false
            }
        }
        return dragListener
    }

    override fun onLongClick(view: View?): Boolean {
        if (view == null || listView.size == 1) {
            /// if only have one view left
            // => drag will not work
            // => message : "Drag is in progress, but there is no drag window handle."
            // => wrong
            return true
        }

        listView.forEach {
            it.onBindMode(BaseViewHolder.ModeBindView.DRAG)
        }
        view?.isInvisible = true
        val item = ClipData.Item((view.parent as View).tag.toString() as CharSequence)
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData((view.parent as View).tag.toString(), mimeTypes, item)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view?.startDragAndDrop(
                data
                , dragShow ?: genDragShadow(view)
                , view
                , DRAG_FLAG_OPAQUE
            )
        } else {
            view?.startDrag(
                    data
                    , dragShow ?: genDragShadow(view)
                    , view
                    , DRAG_FLAG_OPAQUE
            )
        }
        return true
    }

    fun getPairWrapper(event: DragEvent, view: View): PairWrapper{
        val fromView = event.localState as View
        val fromWrapper = fromView.parent as Wrapper
        val toWrapper =
            view.parent as Wrapper
        return PairWrapper(fromWrapper, toWrapper)
    }

    fun swapView(event: DragEvent, view: View){
        var pairWrapper = getPairWrapper(event, view)

        adapter.onSwapDataset(pairWrapper.dragWrapper.position, pairWrapper.effectedWrapper.position)

        adapter.onBindViewHolder(listView[pairWrapper.dragWrapper.position], pairWrapper.dragWrapper.position)
        listView[pairWrapper.dragWrapper.position].onBindMode(BaseViewHolder.ModeBindView.DRAG)
        adapter.onBindViewHolder(listView[pairWrapper.effectedWrapper.position], pairWrapper.effectedWrapper.position)
        listView[pairWrapper.effectedWrapper.position].onBindMode(BaseViewHolder.ModeBindView.DRAG)
    }


    class PairWrapper(var dragWrapper: Wrapper, var effectedWrapper: Wrapper)

    abstract class GridDragDropAdapter<T : BaseViewHolder> {
        internal var callBackGridDatasetChange : (() -> Unit)? = null

        abstract fun onBindViewHolder(holder: T, position: Int)
        abstract fun onCreateViewHolder(parent: ViewGroup): T
        abstract fun getItemCount(): Int
        abstract fun getItemViewType(position: Int): ViewType
        fun notifiDatasetChange() {
            callBackGridDatasetChange?.invoke()
        }
        abstract fun onSwapDataset(fromPosition: Int, toPosition: Int)
    }


}