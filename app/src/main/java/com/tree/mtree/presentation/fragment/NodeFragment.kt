package com.tree.mtree.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tree.mtree.R
import com.tree.mtree.databinding.FragmentNodeBinding
import com.tree.mtree.domain.model.Node
import com.tree.mtree.presentation.MTreeApp
import com.tree.mtree.presentation.viewmodel.ViewModelFactory
import com.tree.mtree.presentation.adapter.NodeAdapter
import com.tree.mtree.presentation.viewmodel.NodeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NodeFragment : Fragment(R.layout.fragment_node) {
    private lateinit var binding: FragmentNodeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val nodeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NodeViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MTreeApp).component
    }

    private lateinit var onFragmentDestroyedListener: OnFragmentDestroyedListener

    private val nodeAdapter = NodeAdapter()

    private var nodeId = UNDEFINED_NODE_ID

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
        if (context is OnFragmentDestroyedListener) {
            onFragmentDestroyedListener = context
        } else {
            throw RuntimeException("Activity must implement OnDestroyedListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNodeBinding.bind(view)

        nodeId = arguments?.getInt(NODE_ID, UNDEFINED_NODE_ID) ?: UNDEFINED_NODE_ID

        setupRecyclerView()
        setupButtons()
        observeViewModel()

        nodeViewModel.getNodes(nodeId)

        if (nodeId == 0) binding.fabBack.visibility = View.INVISIBLE
        else binding.fabBack.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        onFragmentDestroyedListener.onFragmentDestroyed(nodeId)
    }

    private fun observeViewModel() {
        nodeViewModel.nodes.observe(viewLifecycleOwner) {
            nodeAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvNodes.adapter = nodeAdapter

        nodeAdapter.onNodeClick = {
            val nodeId = it.id
            findNavController().navigate(
                NodeFragmentDirections.actionNodeFragmentSelf(nodeId)
            )
        }

        // swipe for delete
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val node = nodeAdapter.currentList[viewHolder.adapterPosition]
                nodeViewModel.deleteNode(node.parentId, node.id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvNodes)
    }

    private fun setupButtons() {
        binding.fabBack.setOnClickListener {
            lifecycleScope.launch {
                val node = nodeViewModel.getNode(nodeId)
                Log.d("NodeId", "parentId = ${node.parentId} и nodeId = ${node.id}")
                findNavController().navigate(
                    NodeFragmentDirections.actionNodeFragmentSelf(node.parentId)
                )
            }
        }

        binding.fabAdd.setOnClickListener {
            nodeViewModel.addNode(
                Node(
                    name = "DEFAULT_NODE_NAME",
                    children = mutableListOf(),
                    parentId = nodeId
                )
            )
        }
    }

    interface OnFragmentDestroyedListener {
        fun onFragmentDestroyed(nodeId: Int)
    }

    companion object {
        private const val NODE_ID = "nodeId"
        private const val UNDEFINED_NODE_ID = 0
    }
}