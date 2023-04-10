package com.tree.mtree.di

import com.tree.mtree.presentation.fragment.NodeFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface NodeComponent {
    fun inject(fragment: NodeFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance nodeId: Int
        ): NodeComponent
    }
}