package com.tree.mtree.domain.repository

import com.tree.mtree.domain.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    suspend fun addNode(node: Node)

    suspend fun deleteNode(parentId: Int, nodeId: Int)

    suspend fun getNode(nodeId: Int): Node

    fun getNodes(parentId: Int): Flow<List<Node>>

    suspend fun updateNodes()
}