package com.nhado.litegriddragdropdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhado.litegriddragdrop.BaseViewHolder
import com.nhado.litegriddragdrop.GridDragDropAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = object :GridDragDropAdapter<MyViewHolder>(){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                var view: View
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
                return MyViewHolder(view)
            }

            override fun getItemCount(): Int = 4

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            }

        }
        gridDragDrop.registerAdapter(adapter)
        gridDragDrop.bindView()
    }
}

class MyViewHolder(itemView: View) : BaseViewHolder(itemView) {

}