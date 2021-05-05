package com.nhado.litegriddragdropdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.GridLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.nhado.litegriddragdrop.BaseViewHolder
import com.nhado.litegriddragdrop.GridDragDrop
import com.nhado.litegriddragdrop.ViewType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_holder.view.*
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = object : GridDragDrop.GridDragDropAdapter<MyViewHolder>(){

            var dataset = arrayListOf(R.drawable.ic_bear, R.drawable.ic_cat, R.drawable.ic_deer, R.drawable.ic_dog, R.drawable.ic_giraffe, R.drawable.ic_jellyfish)

            override fun onCreateViewHolder(parent: ViewGroup): MyViewHolder {
                var view: View
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
                return MyViewHolder(view)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                holder.onBindData(dataset[position]){
                    dataset.removeAt(position)
                    notifiDatasetChange()
                }
            }


            override fun getItemCount(): Int = dataset.size



            override fun getItemViewType(position: Int): ViewType {
                if(position == 0 || position == 3){
                    return ViewType.Builder()
                            .setRowSpec(GridLayout.spec(0,2))
                            .setEmpty(false)
                            .build()
                }
                else
                    return ViewType()
            }

            override fun onSwapDataset(fromPosition: Int, toPosition: Int) {
                Collections.swap(dataset, fromPosition, toPosition)
                Log.d("dataset",dataset.toString())
            }
        }
        gridDragDrop.buildDragShadow = {
            CustomDragShadow(it)
        }
        gridDragDrop.registerAdapter(adapter)
        gridDragDrop.bindView()
        layout.setOnTouchListener { v, event ->
            gridDragDrop.adapter.notifiDatasetChange()
            return@setOnTouchListener true
        }
    }
}

class MyViewHolder(itemView: View) : BaseViewHolder(itemView) {
    var scale = ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    var scaleReverse = ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    init {
        scale.setDuration(100)
        scale.setFillAfter(true)

        scaleReverse.setDuration(100)
        scaleReverse.setFillAfter(true)
    }
    override fun onBindMode(mode: ModeBindView) {
        when(mode){
            ModeBindView.DRAG ->{
                itemView.ic_delete.isVisible = true
                itemView.image_view.startAnimation(scaleReverse);
            }
            ModeBindView.ENTERED ->{
                itemView.image_view.startAnimation(scale);
                itemView.ic_delete.isInvisible = true
            }
            ModeBindView.EXITED ->{
                itemView.image_view.startAnimation(scaleReverse);
                itemView.ic_delete.isVisible = true
            }
        }
    }

    fun onBindData(idRes: Int, onDeleteClicked : ()->Unit) {
        itemView.image_view.setImageResource(idRes)
        itemView.ic_delete.isInvisible = true
        itemView.ic_delete.setOnClickListener {
            onDeleteClicked.invoke()
        }
    }
}