package com.tree.mtree.data.repository

import android.app.Application
import com.tree.mtree.data.database.NodeDatabase
import com.tree.mtree.data.mapper.NodeMapper
import com.tree.mtree.domain.model.Node
import com.tree.mtree.domain.repository.NodeRepository
import java.security.MessageDigest

class NodeRepositoryImpl(application: Application) : NodeRepository {
    private val nodeDao = NodeDatabase.getInstance(application).nodeDao()
    private val nodeMapper = NodeMapper()

    private suspend fun updateNodeName(id: Int) {
        val node = getNode(id)

        val hash = getHashFromNode(node)

        val newNode = node.copy(name = hash)
        nodeDao.updateNodeName(newNode.id, newNode.name)
    }

    private fun getHashFromNode(node: Node): String {
        return "0x" + MessageDigest.getInstance("SHA-256")
            .digest(node.toString().toByteArray())
            .takeLast(20)
            .joinToString("") { byte -> "%02x".format(byte) }
    }

    override suspend fun addNode(node: Node) {
        val nodeDbModel = nodeMapper.mapModelToDbModel(node)
        val nodeId = nodeDao.addNode(nodeDbModel).toInt()

        updateNodeName(nodeId)
    }

    override suspend fun deleteNode(parentId: Int, id: Int) {
        nodeDao.deleteNode(parentId, id)
    }

    override suspend fun getNode(id: Int): Node {
        val nodeDbModel = nodeDao.getNode(id)
        return nodeMapper.mapDbModelToModel(
            nodeDbModel
        )
    }

    override suspend fun getNodes(parentId: Int): List<Node> {
        val nodesDbModel = nodeDao.getNodes(parentId)
        return nodeMapper.mapNodesDbModelToModel(
            nodesDbModel
        )
    }
}