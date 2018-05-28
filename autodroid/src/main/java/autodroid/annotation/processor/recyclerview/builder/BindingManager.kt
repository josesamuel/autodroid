package autodroid.annotation.processor.recyclerview.builder


import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

import javax.tools.Diagnostic.Kind.*

/**
 * Builds each of the classes needed for the RecyclerView autodroid
 */
class BindingManager(private val processingEnvironment: ProcessingEnvironment) {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    /**
     * Generates the helper classes for recycler views
     */
    fun buildRecyclerViewClasses(element: Element) {
        val kaptKotlinGeneratedDir = processingEnvironment.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.replace("kaptKotlin", "kapt")
        ViewHolderBuilder(element, this).build().writeTo(File(kaptKotlinGeneratedDir))
        ViewMappingBuilder(element, this).build().writeTo(File(kaptKotlinGeneratedDir))
        AdapterBuilder(element, this).build().writeTo(File(kaptKotlinGeneratedDir))

    }

    fun log(message: String, kind: Diagnostic.Kind = NOTE) {
        processingEnvironment.messager.printMessage(kind, message)
    }
}
