package com.tree.mtree.domain.usecases

import com.tree.mtree.domain.repository.NodeRepository
import javax.inject.Inject

class GetNodeUseCase @Inject constructor(
    private val nodeRepository: NodeRepository
) {
    suspend operator fun invoke(id: Int) = nodeRepository.getNode(id)
}