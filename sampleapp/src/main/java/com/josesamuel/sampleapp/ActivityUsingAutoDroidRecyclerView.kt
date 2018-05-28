package com.josesamuel.sampleapp

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import autodroid.annotations.recyclerview.AutoRecyclerViewAdapter
import kotlinx.coroutines.experimental.launch
import pager.ListDataProvider
import pager.Pager
import java.util.*


/**
 * A sample activity that uses RecyclerView with AutoDroid generated adapter
 */
class ActivityUsingAutoDroidRecyclerView : Activity() {

    private val random = Random()
    private val listDataProvider = ListDataProvider(listOf(MyData("A", "B", R.drawable.ic_action_name)))
    private val drawables = arrayOf(R.drawable.ic_action_name, R.drawable.ic_action_name2,
            R.drawable.ic_action_name3,
            R.drawable.ic_action_name4,
            R.drawable.ic_action_name5,
            R.drawable.ic_action_name6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@ActivityUsingAutoDroidRecyclerView)
            adapter = MyDataAdapter(Pager(listDataProvider)).apply {
                setOnClickListener { viewId, position ->
                    launch {
                        when (viewId) {
                            R.id.button1 -> listDataProvider.add(random.nextInt(listDataProvider.size), MyData(random.nextInt(100).toString(), random.nextInt(100).toString(), drawables[random.nextInt(drawables.size)]))
                            R.id.button2 -> listDataProvider.removeAt(random.nextInt(listDataProvider.size))
                        }
                    }
                }
            }
        }
    }
}

