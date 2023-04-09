package com.tree.mtree.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tree.mtree.R
import com.tree.mtree.databinding.FragmentNodeBinding
import com.tree.mtree.domain.model.Node
import com.tree.mtree.presentation.adapter.NodeAdapter
import com.tree.mtree.presentation.viewmodel.NodeViewModel
import kotlinx.coroutines.launch

class NodeFragment : Fragment(R.layout.fragment_node) {
    private lateinit var binding: FragmentNodeBinding

    private val nodeViewModel by lazy {
        ViewModelProvider(this)[NodeViewModel::class.java]
    }

    private val nodeAdapter = NodeAdapter()

    private var nodeId = UNDEFINED_NODE_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNodeBinding.bind(view)

        setupRecyclerView()
        setupButtons()
        observeViewModel()

        nodeViewModel.getNodes(nodeId)

        if (nodeId == 0) binding.fabBack.visibility = View.INVISIBLE
        else binding.fabBack.visibility = View.VISIBLE
    }

    private fun observeViewModel() {
        nodeViewModel.nodes.observe(viewLifecycleOwner) {
            nodeAdapter.submitList(it)
            Log.d("Nodes", it.toString())
        }
    }

    private fun setupRecyclerView() {
        binding.rvNodes.adapter = nodeAdapter

        nodeAdapter.onNodeClick = {
            val nodeId = it.id
            val nodeFragment = newInstance(nodeId)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, nodeFragment)
                .commit()
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
                val nodeFragment = newInstance(node.parentId)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, nodeFragment)
                    .commit()
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

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(NODE_ID)) {
            nodeId = args.getInt(NODE_ID, UNDEFINED_NODE_ID)
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    companion object {
        private const val NODE_ID = "node_id"
        private const val UNDEFINED_NODE_ID = 0

        fun newInstance(id: Int): NodeFragment {
            return NodeFragment().apply {
                arguments = Bundle().apply {
                    putInt(NODE_ID, id)
                }
            }
        }
    }
}