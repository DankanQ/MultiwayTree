package com.tree.mtree.domain.repository

import com.tree.mtree.domain.model.Node

interface NodeRepository {
    suspend fun addNode(node: Node)

    suspend fun deleteNode(node: Node)

    suspend fun getNode(parentId: Int, id: Int): Node

    suspend fun getNodes(parentId: Int): List<Node>
}