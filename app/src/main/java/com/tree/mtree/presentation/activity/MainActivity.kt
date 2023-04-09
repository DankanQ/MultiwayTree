package com.tree.mtree.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tree.mtree.R
import com.tree.mtree.presentation.fragment.NodeFragment

class MainActivity : AppCompatActivity(), NodeFragment.OnFragmentDestroyedListener {
    private var nodeId: Int = ROOT_NODE_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchNodeFragment()
    }

    private fun launchNodeFragment() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        nodeId = sharedPrefs.getInt(SHARED_PREFS_KEY, ROOT_NODE_ID)

        val nodeFragment = NodeFragment.newInstance(nodeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, nodeFragment)
            .commit()
    }

    override fun onPause() {
        super.onPause()

        val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt(SHARED_PREFS_KEY, nodeId)
            apply()
        }
    }

    override fun onFragmentDestroyed(nodeId: Int) {
        this@MainActivity.nodeId = nodeId
    }

    companion object {
        private const val SHARED_PREFS_NAME = "node_id_prefs"
        private const val SHARED_PREFS_KEY = "node_id"

        private const val ROOT_NODE_ID = 0
    }
}