package com.tree.mtree.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.treestructure.domain.usecases.AddNodeUseCase
import com.example.treestructure.domain.usecases.DeleteNodeUseCase
import com.example.treestructure.domain.usecases.GetNodeUseCase
import com.example.treestructure.domain.usecases.GetNodesUseCase
import com.tree.mtree.data.repository.NodeRepositoryImpl
import com.tree.mtree.domain.model.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NodeViewModel(application: Application) : AndroidViewModel(application) {
    private val nodeRepositoryImpl = NodeRepositoryImpl(application)

    private val addNodeUseCase = AddNodeUseCase(nodeRepositoryImpl)
    private val deleteNodeUseCase = DeleteNodeUseCase(nodeRepositoryImpl)
    private val getNodeUseCase = GetNodeUseCase(nodeRepositoryImpl)
    private val getNodesUseCase = GetNodesUseCase(nodeRepositoryImpl)

    private val _nodes = MutableLiveData<List<Node>>()
    var nodes: LiveData<List<Node>> = _nodes

    fun addNode(node: Node) {
        viewModelScope.launch(Dispatchers.IO) {
            addNodeUseCase.invoke(node)
        }
    }

    fun deleteNode(parentId: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNodeUseCase.invoke(parentId, id)
        }
    }

    fun getNode(parentId: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getNodeUseCase.invoke(parentId, id)
        }
    }

    fun getNodes(parentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val nodes = getNodesUseCase.invoke(parentId)
            _nodes.postValue(nodes)
        }
    }
}