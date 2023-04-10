package com.tree.mtree.domain.model

data class Node(
    val name: String,
    val children: List<Node>,
    val parentId: Int,
    var nodeId: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
