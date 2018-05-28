package autodroid.annotation.processor.recyclerview.builder

import com.google.auto.common.MoreElements.getPackage
import javax.lang.model.element.Element

/**
 * Base class of all the builders
 */
internal open class RecylerViewBuilder(val element: Element, val bindingManager: BindingManager) {
    /**Package name of the data class*/
    val packageName = getPackage(element).getQualifiedName().toString()
    /**Data class class*/
    val className = element.simpleName.toString()
    /**View holder class*/
    val viewHolderClassName = "${className}ViewHolder"
    /**Builder class name*/
    val builderClassName = "${className}Builder"


}