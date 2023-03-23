package com.example.orgs.di.ui.helper

import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UsuarioHelperModule {
    @Binds
    abstract fun bindUsuarioBaseHelper(usuarioBaseHelperImpl: UsuarioBaseHelperImpl): UsuarioBaseHelper
}