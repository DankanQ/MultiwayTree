package com.tree.mtree.domain.model

data class Node(
    val name: String,
    val children: MutableList<Node>,
    val parentId: Int,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
