package com.tree.mtree.domain.repository

import com.tree.mtree.domain.model.Node

interface NodeRepository {
    suspend fun addNode(node: Node)

    suspend fun deleteNode(parentId: Int, id: Int)

    suspend fun getNode(id: Int): Node

    suspend fun getNodes(parentId: Int): List<Node>
}