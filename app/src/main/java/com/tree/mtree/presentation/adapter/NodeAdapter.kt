package com.tree.mtree.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tree.mtree.databinding.ItemNodeBinding
import com.tree.mtree.domain.model.Node
import com.tree.mtree.presentation.adapter.viewholder.NodeViewHolder

class NodeAdapter : ListAdapter<Node, NodeViewHolder>(NodeDiffCallback) {
    var onNodeClick: ((Node) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val binding = ItemNodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val node = getItem(position)
        with(holder.binding) {
            tvName.text = node.name
            root.setOnClickListener { onNodeClick?.invoke(node) }
        }
    }
}