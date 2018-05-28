package com.josesamuel.sampleapp

import autodroid.annotations.recyclerview.ItemLayoutRes
import autodroid.annotations.recyclerview.ItemIdRes
import autodroid.annotations.recyclerview.ItemType


@ItemLayoutRes(layoutID = R.layout.my_text_view)
data class MyData(@ItemIdRes(viewID = R.id.textView2) val firstName: String,
                  @ItemIdRes(viewID = R.id.textView) val secondName: String,
                  @ItemIdRes(viewID = R.id.imageView, itemType = ItemType.IMAGE) val imageResource: Int,
                  @ItemIdRes(viewID = R.id.button1) val btn1: String = "Add",
                  @ItemIdRes(viewID = R.id.button2) val btn2: String = "Remove")