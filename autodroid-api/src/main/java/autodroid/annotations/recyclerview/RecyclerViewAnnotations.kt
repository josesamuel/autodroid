package autodroid.annotations.recyclerview

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes

/**
 * Specify the layout resource to be used in the view holder for an item.
 * <br/>
 * Annotate this to the data class used for your recycler view
 * <br/>
 *
 *
 *      @ItemLayoutRes(layoutID = R.layout.my_text_view)
 *      data class MyData(
 *          @ItemIdRes(viewID = R.id.textView) val name: String,
 *          @ItemIdRes(viewID = R.id.imageView, itemType = ItemType.IMAGE) val imageResource: Int,
 *          @ItemIdRes(viewID = R.id.button1) val btn1: String = "Add")
 *
 *
 * @param layoutID The layout resource id
 * @see ItemIdRes
 */
@Target(AnnotationTarget.CLASS)
annotation class ItemLayoutRes(@LayoutRes val layoutID: Int)

/**
 * Maps a field in the data class to a view id present in the layout resource for the view holder.
 * <br/>
 * Annotate this to a field in the data class used for your recycler view
 * <br/>
 *
 *
 *      @ItemLayoutRes(layoutID = R.layout.my_text_view)
 *      data class MyData(
 *          @ItemIdRes(viewID = R.id.textView) val name: String,
 *          @ItemIdRes(viewID = R.id.imageView, itemType = ItemType.IMAGE) val imageResource: Int,
 *          @ItemIdRes(viewID = R.id.button1) val btn1: String = "Add")
 *
 *
 * @param viewID The view resource id to map this field to. Must be present in the layout specified by @ItemLayoutRes
 * @param itemType Specify the type of this field. Text type and Image types are supported. Default to Text
 * @see ItemLayoutRes
 * @see ItemType
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
annotation class ItemIdRes(@IdRes val viewID: Int, val itemType: ItemType = ItemType.TEXT)

/**
 * Different types of views supported.
 */
enum class ItemType {
    /**
     * View is a type of TextView
     **/
    TEXT,

    /**
     * View is a type of ImageView.
     * <br/>
     * If the data type of this variable is Bitmap or a Drawable, it is directly set to the ImageView.
     * <br/>
     * If the data type is image resource, File, or Url, autodroid generated code uses Picasso library
     *  to load the image to the ImageView. In that case, add the Picasso dependency to your application
     **/
    IMAGE
}

