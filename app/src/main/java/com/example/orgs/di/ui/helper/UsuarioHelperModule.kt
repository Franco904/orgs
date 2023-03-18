package com.example.orgs.di.ui.helper

import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UsuarioHelperModule {
    @Binds
    abstract fun provideUsuarioHelper(usuarioBaseHelperImpl: UsuarioBaseHelperImpl): UsuarioBaseHelper
}