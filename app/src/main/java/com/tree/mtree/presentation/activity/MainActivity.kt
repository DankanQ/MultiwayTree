package com.tree.mtree.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tree.mtree.R
import com.tree.mtree.presentation.fragment.NodeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchNodeFragment()
    }

    // launch Root Node
    private fun launchNodeFragment() {
        val nodeFragment = NodeFragment.newInstance(0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, nodeFragment)
            .commit()
    }
}