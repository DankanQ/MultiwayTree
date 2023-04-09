package com.tree.mtree.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tree.mtree.R
import com.tree.mtree.databinding.FragmentNodeBinding
import com.tree.mtree.domain.model.Node
import com.tree.mtree.presentation.adapter.NodeAdapter
import com.tree.mtree.presentation.viewmodel.NodeViewModel

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
            val nodeFragment = newInstance(nodeId)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, nodeFragment)
                .commit()
        }
    }

    private fun setupButtons() {
        binding.fabAdd.setOnClickListener {
            nodeViewModel.addNode(
                Node(
                    name = "DEFAULT_NODE_NAME",
                    children = mutableListOf(),
                    parentId = nodeId
                )
            )
            nodeViewModel.getNodes(nodeId)
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