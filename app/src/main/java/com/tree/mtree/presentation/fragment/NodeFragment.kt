package com.tree.mtree.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tree.mtree.R
import com.tree.mtree.databinding.FragmentNodeBinding
import com.tree.mtree.domain.model.Node
import com.tree.mtree.presentation.MTreeApp
import com.tree.mtree.presentation.adapter.NodeAdapter
import com.tree.mtree.presentation.viewmodel.NodeViewModel
import com.tree.mtree.presentation.viewmodel.ViewModelFactory
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
            .nodeComponentFactory()
    }

    private lateinit var onFragmentDestroyedListener: OnFragmentDestroyedListener

    private val nodeAdapter by lazy {
        NodeAdapter()
    }

    private var nodeId = UNDEFINED_NODE_ID

    override fun onAttach(context: Context) {
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
        component.create(nodeId).inject(this)

        setupRecyclerView()
        setupButtons()
        observeViewModel()

        if (nodeId == 0) binding.fabBack.visibility = View.INVISIBLE
        else binding.fabBack.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        onFragmentDestroyedListener.onFragmentDestroyed(nodeId)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                nodeViewModel.state.collect {
                    when (it) {
                        is NodeViewModel.State.Loaded -> {
                            with(binding) {
                                tvEmptyList.isVisible = false
                                tvErrorMessage.isVisible = false
                            }
                            nodeAdapter.submitList(it.nodes)
                            binding.rvNodes.isVisible = true
                        }
                        is NodeViewModel.State.Error -> {
                            with(binding) {
                                tvEmptyList.isVisible = false
                                rvNodes.isVisible = false
                                tvErrorMessage.text = it.error
                                tvErrorMessage.isVisible = true
                            }
                        }
                        is NodeViewModel.State.Empty -> {
                            with(binding) {
                                rvNodes.isVisible = false
                                tvErrorMessage.isVisible = false
                                tvEmptyList.isVisible = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvNodes.adapter = nodeAdapter

        nodeAdapter.onNodeClick = {
            val nodeId = it.nodeId
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
                nodeViewModel.deleteNode(node.parentId, node.nodeId)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvNodes)
    }

    private fun setupButtons() {
        binding.fabBack.setOnClickListener {
            lifecycleScope.launch {
                val node = nodeViewModel.getNode(nodeId)

                findNavController().navigate(
                    NodeFragmentDirections.actionNodeFragmentSelf(node.parentId)
                )
            }
        }

        binding.fabAdd.setOnClickListener {
            nodeViewModel.addNode(
                Node(
                    name = UNDEFINED_NAME,
                    children = listOf(),
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
        private const val UNDEFINED_NAME = "DEFAULT_NODE_NAME"
    }
}