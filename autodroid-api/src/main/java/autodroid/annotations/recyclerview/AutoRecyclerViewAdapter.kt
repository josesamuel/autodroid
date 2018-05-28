package autodroid.annotations.recyclerview

/**
 * Interface implemented by the auto generated Adapter
 */
interface AutoRecyclerViewAdapter {

    /**
     * Set this to get call back when an item in the view holder is clicked.
     *
     * @param onClickListener The listener to get call back
     */
    fun setOnClickListener(onClickListener: AutoRecyclerViewOnClickListener)

}

/**
 * Gets call back when an item is clicked.
 *
 * @param viewId The id of the view clicked
 * @param position The position of the item in adapter.
 * @see AutoRecyclerViewAdapter.setOnClickListener
 */
typealias AutoRecyclerViewOnClickListener = (viewId: Int, position: Int) -> Unit