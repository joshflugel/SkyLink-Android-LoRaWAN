package com.lora.skylink.di

import androidx.fragment.app.Fragment
import com.lora.skylink.presentation.common.PermissionsRequester
import com.lora.skylink.presentation.common.PermissionsRequesterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object DiFragmentModule {

    @Provides
    fun providePermissionsRequesterFactory(fragment: Fragment): PermissionsRequesterFactory {
        return PermissionsRequesterFactory(fragment)
    }

    @Provides
    fun providePermissionsRequester(factory: PermissionsRequesterFactory): PermissionsRequester {
        return factory.create()
    }
}