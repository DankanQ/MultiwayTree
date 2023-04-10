package com.tree.mtree.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository
import javax.inject.Inject

class GetNodesUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    operator fun invoke(parentId: Int) = nodeRepository.getNodes(parentId)
}