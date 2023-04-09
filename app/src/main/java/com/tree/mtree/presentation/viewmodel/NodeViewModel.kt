package com.tree.mtree.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treestructure.domain.usecases.AddNodeUseCase
import com.example.treestructure.domain.usecases.DeleteNodeUseCase
import com.example.treestructure.domain.usecases.GetNodeUseCase
import com.example.treestructure.domain.usecases.GetNodesUseCase
import com.tree.mtree.domain.model.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class NodeViewModel @Inject constructor(
    private val addNodeUseCase: AddNodeUseCase,
    private val deleteNodeUseCase: DeleteNodeUseCase,
    private val getNodeUseCase: GetNodeUseCase,
    private val getNodesUseCase: GetNodesUseCase
) : ViewModel() {
    private val _nodes = MutableLiveData<List<Node>>()
    var nodes: LiveData<List<Node>> = _nodes

    private val _node = MutableLiveData<Node>()
    var node: LiveData<Node> = _node

    fun addNode(node: Node) {
        viewModelScope.launch(Dispatchers.IO) {
            addNodeUseCase.invoke(node)
            getNodes(node.parentId)
        }
    }

    fun deleteNode(parentId: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNodeUseCase.invoke(parentId, id)
            getNodes(parentId)
        }
    }

    suspend fun getNode(id: Int): Node {
        return viewModelScope.async(Dispatchers.IO) {
            getNodeUseCase.invoke(id)
        }.await()
    }

    fun getNodes(parentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val nodes = getNodesUseCase.invoke(parentId)
            _nodes.postValue(nodes)
        }
    }
}