package com.tree.mtree.presentation

import android.app.Application
import com.tree.mtree.di.DaggerAppComponent

class MTreeApp: Application() {
    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }
}