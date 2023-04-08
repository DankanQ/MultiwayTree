package com.tree.mtree.data.mapper

import com.tree.mtree.data.database.model.NodeDbModel
import com.tree.mtree.domain.model.Node

class NodeMapper {
    fun mapModelToDbModel(node: Node) = NodeDbModel(
        id = node.id,
        name = node.name,
        children = mapNodesModelToDbModel(node.children),
        parentId = node.parentId
    )

    fun mapDbModelToModel(nodeDbModel: NodeDbModel) = Node(
        id = nodeDbModel.id,
        name = nodeDbModel.name,
        children = mapNodesDbModelToModel(nodeDbModel.children),
        parentId = nodeDbModel.parentId
    )

    private fun mapNodesModelToDbModel(nodes: MutableList<Node>): MutableList<NodeDbModel> {
        return nodes.map {
            mapModelToDbModel(it)
        }.toMutableList()
    }

    fun mapNodesDbModelToModel(nodesDbModel: MutableList<NodeDbModel>): MutableList<Node> {
        return nodesDbModel.map {
            mapDbModelToModel(it)
        }.toMutableList()
    }
}