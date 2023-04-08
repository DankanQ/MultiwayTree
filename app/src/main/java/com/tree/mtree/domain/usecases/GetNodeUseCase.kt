package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository

class GetNodeUseCase(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(parentId: Int, id: Int) = nodeRepository.getNode(parentId, id)
}