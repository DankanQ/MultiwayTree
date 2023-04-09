package com.example.treestructure.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository
import javax.inject.Inject

class DeleteNodeUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(parentId: Int, id: Int) = nodeRepository.deleteNode(parentId, id)
}