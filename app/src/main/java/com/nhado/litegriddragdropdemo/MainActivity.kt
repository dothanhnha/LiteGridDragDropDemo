package com.nhado.litegriddragdropdemo

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.nhado.litegriddragdrop.BaseViewHolder
import com.nhado.litegriddragdrop.GridDragDrop
import com.nhado.litegriddragdrop.ViewType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_holder.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = object : GridDragDrop.GridDragDropAdapter<MyViewHolder>(){

            var dataset = arrayListOf("item1", "item2", "item3", "item4", "item5", "item6")

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
        gridDragDrop.registerAdapter(adapter)
        gridDragDrop.bindView()
    }
}

class MyViewHolder(itemView: View) : BaseViewHolder(itemView) {
    override fun onBindMode(mode: ModeBindView) {
    }

    fun onBindData(data: String, onDeleteClicked : ()->Unit) {
        itemView.textView.text = data
        itemView.ic_delete.setOnClickListener {
            onDeleteClicked.invoke()
        }
    }
}