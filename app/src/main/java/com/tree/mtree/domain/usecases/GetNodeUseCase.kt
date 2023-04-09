package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository

class GetNodeUseCase(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(id: Int) = nodeRepository.getNode(id)
}