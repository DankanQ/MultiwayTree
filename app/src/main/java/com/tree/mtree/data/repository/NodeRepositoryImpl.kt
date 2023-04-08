package com.tree.mtree.data.repository

import android.app.Application
import com.example.treestructure.data.database.NodeDatabase
import com.tree.mtree.data.mapper.NodeMapper
import com.tree.mtree.domain.model.Node
import com.tree.mtree.domain.repository.NodeRepository

class NodeRepositoryImpl(application: Application) : NodeRepository {
    private val nodeDao = NodeDatabase.getInstance(application).nodeDao()
    private val nodeMapper = NodeMapper()

    override suspend fun addNode(node: Node) {
        nodeDao.addNode(nodeMapper.mapModelToDbModel(node))
    }

    override suspend fun deleteNode(parentId: Int, id: Int) {
        nodeDao.deleteNode(parentId, id)
    }

    override suspend fun getNode(parentId: Int, id: Int): Node {
        val nodeDbModel = nodeDao.getNode(parentId, id)
        return nodeMapper.mapDbModelToModel(nodeDbModel)
    }

    override suspend fun getNodes(parentId: Int): List<Node> {
        val nodesDbModel = nodeDao.getNodes(parentId)
        return nodeMapper.mapNodesDbModelToModel(nodesDbModel)
    }
}