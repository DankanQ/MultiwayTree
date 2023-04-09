package com.tree.mtree.di

import android.app.Application
import com.tree.mtree.presentation.MTreeApp
import com.tree.mtree.presentation.activity.MainActivity
import com.tree.mtree.presentation.fragment.NodeFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: MTreeApp)

    fun inject(activity: MainActivity)

    fun inject(nodeFragment: NodeFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}