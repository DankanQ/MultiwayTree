package com.tree.mtree.di

import android.app.Application
import com.tree.mtree.presentation.MTreeApp
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [DataModule::class])
interface AppComponent {
    fun inject(application: MTreeApp)

    fun nodeIdComponentFactory(): NodeIdComponent.Factory

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}