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
    private suspend fun updateNodeName(nodeId: Int) {
        val node = getNode(nodeId)

        val hash = getHashFromNode(node)

        val newNode = node.copy(name = hash)
        nodeDao.updateNodeName(newNode.nodeId, newNode.name)
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

    override suspend fun deleteNode(parentId: Int, nodeId: Int) {
        nodeDao.deleteNode(parentId, nodeId)
    }

    override suspend fun getNode(nodeId: Int): Node {
        val nodeDbModel = nodeDao.getNode(nodeId)
        return nodeMapper.mapDbModelToModel(
            nodeDbModel
        )
    }

    private val updateEvents = MutableSharedFlow<Unit>()

    override fun getNodes(parentId: Int): Flow<List<Node>> = flow {
        emit(nodeMapper.mapNodesDbModelToModel(
            nodeDao.getNodes(parentId)).toList()
        )
        updateEvents.collect {
            emit(nodeMapper.mapNodesDbModelToModel(
                nodeDao.getNodes(parentId)).toList()
            )
        }
    }

    override suspend fun updateNodes() {
        updateEvents.emit(Unit)
    }
}