package com.tree.mtree.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tree.mtree.domain.model.Node
import com.tree.mtree.domain.usecases.AddNodeUseCase
import com.tree.mtree.domain.usecases.DeleteNodeUseCase
import com.tree.mtree.domain.usecases.GetNodeUseCase
import com.tree.mtree.domain.usecases.GetNodesUseCase
import com.tree.mtree.domain.usecases.UpdateNodesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NodeViewModel @Inject constructor(
    private val addNodeUseCase: AddNodeUseCase,
    private val deleteNodeUseCase: DeleteNodeUseCase,
    private val getNodeUseCase: GetNodeUseCase,
    getNodesUseCase: GetNodesUseCase,
    nodeId: Int,
    private val updateNodesUseCase: UpdateNodesUseCase
) : ViewModel() {
    val state: Flow<State> = getNodesUseCase(nodeId)
        .map {
            if (it.isNotEmpty()) {
                State.Loaded(nodes = it) as State
            } else {
                State.Empty
            }
        }
        .catch { emit(State.Error(it.message.toString())) }
        .shareIn(
            viewModelScope,
            SharingStarted.Lazily,
            1
        )

    fun addNode(node: Node) {
        viewModelScope.launch(Dispatchers.IO) {
            addNodeUseCase(node)
            shouldUpdateNodes()
        }
    }

    fun deleteNode(parentId: Int, nodeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNodeUseCase(parentId, nodeId)
            shouldUpdateNodes()
        }
    }

    suspend fun getNode(nodeId: Int): Node {
        return withContext(Dispatchers.IO) {
            getNodeUseCase(nodeId)
        }
    }

    private fun shouldUpdateNodes() {
        viewModelScope.launch {
            updateNodesUseCase()
        }
    }

    sealed interface State {
        data class Loaded(val nodes: List<Node>) : State
        data class Error(val error: String) : State
        object Empty : State
    }
}