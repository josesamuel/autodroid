# AutoDroid

AutoDroid automates some common Android development tasks

## 1. RecyclerView

RecylerView is awesome, but it require you to create several boilerplate code just to make it work -- such as 

1. Create an Adapter class
2. Create a ViewHolder class
3. Declare and initialize the ViewHolder with all the views
4. Implement Adapter methods such as
	1. onCreateViewHolder 
	2. onBindViewHolder - Bind each of the views in ViewHolder with the data  

In Code it looks something like - 

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
		
		.....
		
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(dataSet)
        
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager

            adapter = viewAdapter
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
    
```
<br/><br/>

---

With **AutoDroid**, all you have to do is : 


```kotlin
//Set the adapter to the AutoDroid auto generated class
recyclerView.adapter = MyDataAdapter(Pager(listDataProvider))

```

---

* No need to write Adapter, AutoDroid creates it for you 
* No need to write ViewHolder, AutoDroid internally takes care of it
* No need of manually binding each view to data, AutoDroid takes care of it
* All you have to do is annotate your data class with the layout and view bindings
* No need of manually notifying adapter when data changes. Adapter generated by AutoDroid, listens for the data added/removed events takes care of it
* Supports Text and Image data types
* Uses Picaso to load images as needed
* Uses [Pager](https://josesamuel.com/pager/) to manage data, allowing you to easily add/remove/update data

To use AutoDroid, simply annotate your data class for the recycler as follows

```kotlin
@ItemLayoutRes(layoutID = R.layout.itemView)
data class MyData(@ItemIdRes(viewID = R.id.textView) val firstName: String,
                  @ItemIdRes(viewID = R.id.imageView, itemType = ItemType.IMAGE) val imageResource: Int)
```


AutoDroid will generate the Adapter for you (In this case MyDataAdapter). Simply set this as the adapter of your recycler view. 

See [example](https://github.com/josesamuel/autodroid/blob/master/sampleapp/src/main/java/com/josesamuel/sampleapp/ActivityUsingAutoDroidRecyclerView.kt)

That's it! 


Getting AutoDroid
--------

Gradle dependency

```groovy
dependencies {
    implementation 'com.josesamuel:autodroid-api:1.0.1'
    kapt 'com.josesamuel: autodroid:1.0.1'
    implementation 'com.josesamuel:pager:1.0.4'
    
    //Add Picaso if the any data binding uses Image type
    implementation 'com.squareup.picasso:picasso:2.71828'
    
}
```


License
-------

    Copyright 2018 Joseph Samuel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


