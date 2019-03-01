package autodroid.annotation.processor.recyclerview.builder

import autodroid.annotations.recyclerview.ItemIdRes
import autodroid.annotations.recyclerview.ItemType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element


/**
 * Builds the <DataClass>ViewHolder class to be used for the RecyclerView
 */
internal class ViewHolderBuilder(element: Element, bindingManager: BindingManager) : RecylerViewBuilder(element, bindingManager) {

    /**
     * Returns the file spec for the view holder
     */
    fun build(): FileSpec {
        val classSpecBuilder = TypeSpec.classBuilder(viewHolderClassName)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter("mainView", ClassName("android.view", "View"))
                        .addParameter("private var onClickListener", ClassName("autodroid.annotations.recyclerview", "AutoRecyclerViewOnClickListener").asNullable())
                        .build())
                .superclass(ClassName("android.support.v7.widget", "RecyclerView", "ViewHolder"))
                .addSuperclassConstructorParameter("mainView")

        val initBuilder = CodeBlock.builder()


        element.enclosedElements.filter { it.getAnnotation(ItemIdRes::class.java) != null }.forEach {
            val fieldName = it.simpleName.toString()
            val annotation = it.getAnnotation(ItemIdRes::class.java)
            if (annotation.itemType == ItemType.TEXT) {
                classSpecBuilder.addProperty(PropertySpec.builder(fieldName, ClassName("android.widget", "TextView")).initializer("mainView.findViewById(" + annotation.viewID + ")").build())
            } else {
                classSpecBuilder.addProperty(PropertySpec.builder(fieldName, ClassName("android.widget", "ImageView")).initializer("mainView.findViewById(" + annotation.viewID + ")").build())
            }
            initBuilder.addStatement("$fieldName.setOnClickListener(View.OnClickListener { onClickListener?.invoke($fieldName.id, adapterPosition) })")
        }

        initBuilder.addStatement("mainView.setOnClickListener(View.OnClickListener { onClickListener?.invoke(mainView.id, adapterPosition) })")

        classSpecBuilder.addInitializerBlock(initBuilder.build())

        val fileSpecBuilder = FileSpec.builder(packageName, viewHolderClassName)
                .addType(classSpecBuilder.build())

        return fileSpecBuilder.build()
    }

}