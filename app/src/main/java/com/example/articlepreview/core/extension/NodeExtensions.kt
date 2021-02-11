package com.example.articlepreview.core.extension

import org.commonmark.node.Block
import org.commonmark.node.Image
import org.commonmark.node.Node

/** Node内から[Image]を探してリストで返す */
fun Node.imageNodes(): List<Image> {

    val images = mutableListOf<Image>()
    var child = firstChild

    while (child != null) {
        if (child is Image) {
            images + child
        }
        child = child.next
    }

    return images
}

/** Node内にソースコードのBlockがあるかどうかを判定 */
fun Node.hasSourceCodeBlock(): Boolean {
    var child = firstChild

    while (child != null) {
        if (child is Block) {
            return true
        }
        child = child.next
    }

    return false
}