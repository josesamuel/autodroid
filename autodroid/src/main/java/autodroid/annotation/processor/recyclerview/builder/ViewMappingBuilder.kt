package autodroid.annotation.processor.recyclerview.builder

import autodroid.annotations.recyclerview.ItemIdRes
import autodroid.annotations.recyclerview.ItemType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

/**
 * Builds the <DataClass>Builder class to be used for the RecyclerView to map the view holder to the data
 */
internal class ViewMappingBuilder(element: Element, bindingManager: BindingManager) : RecylerViewBuilder(element, bindingManager) {


    fun build(): FileSpec {


        val methodSpecBuilder = FunSpec.builder("bind")
                .addParameter("viewHolder", ClassName(packageName, viewHolderClassName))
                .addParameter("data", element.asType().asTypeName())

        element.enclosedElements.filter { it.getAnnotation(ItemIdRes::class.java) != null }.forEach {
            val fieldName = it.simpleName.toString()
            if (!it.modifiers.contains(Modifier.PRIVATE)
                    || element.enclosedElements.firstOrNull() {
                        it.kind == ElementKind.METHOD && it.simpleName.toString().equals("get$fieldName", true)
                    } != null
            ) {
                if (it.getAnnotation(ItemIdRes::class.java).itemType == ItemType.TEXT) {
                    methodSpecBuilder.addStatement("viewHolder.$fieldName.text = data.$fieldName")
                } else {
                    when (it.asType().toString()) {
                        "android.graphics.Bitmap" -> methodSpecBuilder.addStatement("viewHolder.$fieldName.setImageBitmap(data.$fieldName)")
                        "android.graphics.drawable.Drawable" -> methodSpecBuilder.addStatement("viewHolder.$fieldName.setImageDrawable(data.$fieldName)")
                        else -> methodSpecBuilder.addStatement("%T.get().load(data.$fieldName).into(viewHolder.$fieldName)", ClassName("com.squareup.picasso", "Picasso"))
                    }
                }
            }

        }

        val viewHolderCreatedLambda = LambdaTypeName.get(parameters = *arrayOf(ClassName(packageName, viewHolderClassName)),
                returnType = Unit::class.asClassName())

        val viewHolderBindLambda = LambdaTypeName.get(parameters = *arrayOf(ClassName(packageName, viewHolderClassName),
                element.asType().asTypeName()),
                returnType = Unit::class.asClassName())

        val fileSpecBuilder = FileSpec.builder(packageName, builderClassName)
                .addFunction(methodSpecBuilder.build())
                .addTypeAlias(TypeAliasSpec.builder("${viewHolderClassName}CreatedListener", viewHolderCreatedLambda)
                        .addKdoc("Gets call back when a view holder is created.\nOverride if needed to do any customization\n")
                        .build())
                .addTypeAlias(TypeAliasSpec.builder("${viewHolderClassName}BindListener", viewHolderBindLambda)
                        .addKdoc("Gets call back when a view holder is bound. \n" +
                                 "Override if needed to do any customization\n")
                        .build())




        return fileSpecBuilder.build()
    }
}