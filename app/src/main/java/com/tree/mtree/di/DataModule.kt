package com.tree.mtree.di

import android.app.Application
import com.tree.mtree.data.database.NodeDao
import com.tree.mtree.data.database.NodeDatabase
import com.tree.mtree.data.repository.NodeRepositoryImpl
import com.tree.mtree.domain.repository.NodeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    @AppScope
    fun bindNodeRepository(impl: NodeRepositoryImpl): NodeRepository

    companion object {
        @Provides
        @AppScope
        fun provideNodeDao(application: Application): NodeDao {
            return NodeDatabase.getInstance(application).nodeDao()
        }
    }
}