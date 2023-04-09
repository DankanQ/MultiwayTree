package com.tree.mtree.di

import androidx.lifecycle.ViewModel
import com.tree.mtree.presentation.viewmodel.NodeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(NodeViewModel::class)
    @Binds
    fun bindNodeViewModel(impl: NodeViewModel): ViewModel
}