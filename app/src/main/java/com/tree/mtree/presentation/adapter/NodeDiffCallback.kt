package com.tree.mtree.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tree.mtree.domain.model.Node

object NodeDiffCallback : DiffUtil.ItemCallback<Node>() {
    override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean {
        return oldItem.nodeId == newItem.nodeId
    }

    override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean {
        return oldItem == newItem
    }
}