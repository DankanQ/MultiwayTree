package com.tree.mtree.data.mapper

import com.tree.mtree.data.database.model.NodeDbModel
import com.tree.mtree.domain.model.Node
import javax.inject.Inject

class NodeMapper @Inject constructor() {
    fun mapModelToDbModel(node: Node) = NodeDbModel(
        nodeId = node.nodeId,
        name = node.name,
        children = mapNodesModelToDbModel(node.children),
        parentId = node.parentId
    )

    fun mapDbModelToModel(nodeDbModel: NodeDbModel) = Node(
        nodeId = nodeDbModel.nodeId,
        name = nodeDbModel.name,
        children = mapNodesDbModelToModel(nodeDbModel.children),
        parentId = nodeDbModel.parentId
    )

    private fun mapNodesModelToDbModel(nodes: List<Node>): List<NodeDbModel> {
        return nodes.map {
            mapModelToDbModel(it)
        }
    }

    fun mapNodesDbModelToModel(nodesDbModel: List<NodeDbModel>): List<Node> {
        return nodesDbModel.map {
            mapDbModelToModel(it)
        }
    }
}