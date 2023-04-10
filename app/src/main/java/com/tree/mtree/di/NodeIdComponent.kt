package com.tree.mtree.di

import com.tree.mtree.presentation.fragment.NodeFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface NodeIdComponent {
    fun inject(fragment: NodeFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @NodeIdQualifier nodeId: Int
        ): NodeIdComponent
    }
}