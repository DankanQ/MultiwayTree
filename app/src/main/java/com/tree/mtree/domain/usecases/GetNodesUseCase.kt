package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository

class GetNodesUseCase(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(parentId: Int) = nodeRepository.getNodes(parentId)
}