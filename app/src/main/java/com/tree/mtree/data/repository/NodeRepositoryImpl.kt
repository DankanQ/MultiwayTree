package com.tree.mtree.data.repository

import com.tree.mtree.data.database.NodeDao
import com.tree.mtree.data.mapper.NodeMapper
import com.tree.mtree.domain.model.Node
import com.tree.mtree.domain.repository.NodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import javax.inject.Inject

class NodeRepositoryImpl @Inject constructor(
    private val nodeDao: NodeDao,
    private val nodeMapper: NodeMapper
) : NodeRepository {
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

    private val refreshEvents = MutableSharedFlow<Unit>()

    override fun getNodes(parentId: Int): Flow<List<Node>> = flow {
        val nodesDbModel = nodeDao.getNodes(parentId)
        emit(nodeMapper.mapNodesDbModelToModel(nodesDbModel))
        refreshEvents.collect {
            val nodesDbModel = nodeDao.getNodes(parentId)
            emit(nodeMapper.mapNodesDbModelToModel(nodesDbModel))
        }
    }

    override suspend fun updateNodes() {
        refreshEvents.emit(Unit)
    }
}