package com.josesamuel.sampleapp

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_text_view.view.*
import kotlinx.coroutines.experimental.launch
import java.util.*


class ActivityWithoutAutoDroidRecyclerView : Activity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val dataSet = mutableListOf(MyData("A", "B", R.drawable.ic_action_name))

    private val drawables = arrayOf(R.drawable.ic_action_name, R.drawable.ic_action_name2,
            R.drawable.ic_action_name3,
            R.drawable.ic_action_name4,
            R.drawable.ic_action_name5,
            R.drawable.ic_action_name6)
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        dataSet.add(MyData(random.nextInt(100).toString(), random.nextInt(100).toString(), drawables[random.nextInt(drawables.size)]))
        viewAdapter = MyAdapter(dataSet)
        (viewAdapter as MyAdapter).setOnClickListener(View.OnClickListener {
            launch {
                when (it.id) {
                    R.id.button1 -> dataSet.add(random.nextInt(dataSet.size),MyData(random.nextInt(100).toString(), random.nextInt(100).toString(), drawables[random.nextInt(drawables.size)]))
                    R.id.button2 -> dataSet.removeAt(random.nextInt(dataSet.size))
                }

                runOnUiThread { viewAdapter.notifyDataSetChanged() }
            }

        })


        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager

            adapter = viewAdapter
        }

    }
}


/**
 * Adapter for the recycler view
 */
class MyAdapter(private val myDataset: MutableList<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var onClickListener: View.OnClickListener? = null;

    //********************************************************************************
    /**
     * View holder for recycler view
     */
    class ViewHolder(mainView: View, private val onClickListener: View.OnClickListener) : RecyclerView.ViewHolder(mainView) {

        /**
         * Keep hold of the first name text view
         */
        val firstName: TextView = mainView.findViewById(R.id.textView)

        /**
         * Keep hold of the second name text view
         */
        val secondName: TextView = mainView.findViewById(R.id.textView2)

        /**
         * Keep hold of the image view for image resource
         */
        val imageResource: ImageView = mainView.findViewById(R.id.imageView)

        /**
         * Keep hold of the add button to add click listener
         */
        val addBtn: Button = mainView.findViewById(R.id.button1)

        /**
         * Keep hold of the remove button to add click listener
         */
        val removeBtn: Button = mainView.findViewById(R.id.button2)


        init {
            //add the click listeners
            addBtn.setOnClickListener { onClickListener.onClick(addBtn) }
            removeBtn.setOnClickListener { onClickListener.onClick(removeBtn) }
        }
    }
    //********************************************************************************


    fun setOnClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * Create the view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false),
                View.OnClickListener {
                    onClickListener?.onClick(it)
                })
    }

    /**
     * Bind the view holder with the data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind the first name with the data
        holder.firstName.text = myDataset[position].firstName

        //bind the second name with the data
        holder.secondName.text = myDataset[position].secondName

        //bind the image using picaso
        Picasso.get().load(myDataset[position].imageResource).into(holder.imageResource)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }
}

