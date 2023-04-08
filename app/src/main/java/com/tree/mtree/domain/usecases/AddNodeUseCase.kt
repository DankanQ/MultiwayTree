package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.model.Node
import com.tree.mtree.domain.repository.NodeRepository

class AddNodeUseCase(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(node: Node) = nodeRepository.addNode(node)
}