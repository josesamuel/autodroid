package autodroid.annotation.processor.recyclerview.builder

import autodroid.annotations.recyclerview.ItemLayoutRes
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element

/**
 * Builds the <DataClass>Adapter class to be used for the RecyclerView
 */
internal class AdapterBuilder(element: Element, bindingManager: BindingManager) : RecylerViewBuilder(element, bindingManager) {

    private val adapterClassName = "${className}Adapter"

    /**
     * Build  and returns the FileSpec
     */
    fun build(): FileSpec {
        val classSpecBuilder = TypeSpec.classBuilder(adapterClassName)
                .addKdoc("Adapter that shows the data of  [%T]\n\n" +
                        "@param data Pager with the data\n", ClassName.bestGuess(className))
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addKdoc("Create an adapter with the given [Pager] that can be updated")
                        .addParameter("val data", ParameterizedTypeName.get(ClassName("pager", "Pager"), ClassName.bestGuess(className)))
                        .build())
                //Extends the RecyclerView.Adapter
                .superclass(ClassName("android.support.v7.widget", "RecyclerView", "Adapter<$viewHolderClassName>"))
                //Implements AutoRecyclerViewAdapter
                .addSuperinterface(ClassName("autodroid.annotations.recyclerview", "AutoRecyclerViewAdapter"))
                //Implements PagerListener
                .addSuperinterface(ParameterizedTypeName.get(ClassName("pager", "PagerListener"), ClassName.bestGuess(className)))

                //Initializer
                .addInitializerBlock(CodeBlock.builder().addStatement("data.setPagerListener(this)").build())

                //Click listener
                .addProperty(PropertySpec.varBuilder("onClickListener", ClassName("autodroid.annotations.recyclerview", "AutoRecyclerViewOnClickListener").asNullable(), KModifier.PRIVATE).initializer("null").build())

                //Handler
                .addProperty(PropertySpec.builder("handler", ClassName("android.os", "Handler"), KModifier.PRIVATE).initializer("Handler(%T.getMainLooper())", ClassName("android.os", "Looper")).build())

                //ViewHolderCreated listener
                .addProperty(PropertySpec.builder("viewHolderCreatedListener", TypeVariableName("${className}ViewHolderCreatedListener").asNullable(), KModifier.PUBLIC).mutable(true).initializer("null").build())

                //ViewHolderBound listener
                .addProperty(PropertySpec.builder("viewHolderBindListener", TypeVariableName("${className}ViewHolderBindListener").asNullable(), KModifier.PUBLIC).mutable(true).initializer("null").build())

                //secondary constructor
                .addFunction(FunSpec.constructorBuilder()
                        .addParameter("data", ParameterizedTypeName.get(List::class.asTypeName(), ClassName.bestGuess(className)))
                        .callThisConstructor(CodeBlock.of("Pager(%T(data))", ClassName("pager", "ListDataProvider")))
                        .addKdoc("Create an adapter with the given list of data\n")
                        .build())

                //Override the methods

                .addFunction(FunSpec.builder("onCreateViewHolder").addModifiers(KModifier.OVERRIDE)
                        .addParameter("parent", ClassName("android.view", "ViewGroup"))
                        .addParameter("viewType", Int::class.java)
                        .addStatement("return %T(%T.from(parent.context).inflate(" + element.getAnnotation(ItemLayoutRes::class.java).layoutID + ", parent, false), { viewId, position -> onClickListener?.invoke(viewId, position) } ).also {" +
                                " viewHolderCreatedListener?.invoke(it) }",
                                ClassName(packageName, viewHolderClassName),
                                ClassName("android.view", "LayoutInflater"))
                        .build())

                .addFunction(FunSpec.builder("getItemCount").addModifiers(KModifier.OVERRIDE)
                        .addStatement("return data.size()")
                        .build())

                .addFunction(FunSpec.builder("runOnUIThread").addModifiers(KModifier.PRIVATE)
                        .addParameter("body", LambdaTypeName.get(returnType = ANY))
                        .beginControlFlow("if (Looper.getMainLooper().isCurrentThread)")
                        .addStatement("body.invoke()")
                        .endControlFlow()
                        .beginControlFlow("else")
                        .addStatement("handler.post { body.invoke() }")
                        .endControlFlow()
                        .build())


                .addFunction(FunSpec.builder("setOnClickListener").addModifiers(KModifier.OVERRIDE)
                        .addParameter("onClickListener", ClassName("autodroid.annotations.recyclerview", "AutoRecyclerViewOnClickListener"))
                        .addStatement("this.onClickListener = onClickListener")
                        .build())

                .addFunction(FunSpec.builder("onBindViewHolder").addModifiers(KModifier.OVERRIDE)
                        .addParameter("holder", ClassName(packageName, viewHolderClassName))
                        .addParameter("position", Int::class.java)
                        .addStatement("return bind(holder, data[position]).also {" +
                                " viewHolderBindListener?.invoke(holder, data[position]) }")
                        .build())


                .addFunction(FunSpec.builder("onDataAdded").addModifiers(KModifier.OVERRIDE)
                        .addParameter("newData", ClassName.bestGuess(className))
                        .addParameter("index", Int::class.java)
                        .addStatement("runOnUIThread { notifyItemInserted(index) }")
                        .build())


                .addFunction(FunSpec.builder("onDataRemoved").addModifiers(KModifier.OVERRIDE)
                        .addParameter("index", Int::class.java)
                        .addStatement("runOnUIThread { notifyItemRemoved(index) }")
                        .build())

                .addFunction(FunSpec.builder("onDataSetChanged").addModifiers(KModifier.OVERRIDE)
                        .addStatement("runOnUIThread { notifyDataSetChanged() }")
                        .build())

                .addFunction(FunSpec.builder("onDataReplaced").addModifiers(KModifier.OVERRIDE)
                        .addParameter("oldData", ClassName.bestGuess(className))
                        .addParameter("newData", ClassName.bestGuess(className))
                        .addStatement("runOnUIThread { notifyDataSetChanged() }")
                        .build())

        val fileSpecBuilder = FileSpec.builder(packageName, adapterClassName)
                .addType(classSpecBuilder.build())

        return fileSpecBuilder.build()
    }

}