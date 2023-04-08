package com.tree.mtree.domain.model

data class Node(
    val id: Int,
    val name: String,
    val children: MutableList<Node>,
    val parentId: Int
)
