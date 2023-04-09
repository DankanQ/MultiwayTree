package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository
import javax.inject.Inject

class GetNodesUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(parentId: Int) = nodeRepository.getNodes(parentId)
}