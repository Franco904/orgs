package com.example.orgs.di.infra

import com.example.orgs.contracts.infra.preferences.UsuariosPreferences
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InfraModule {
    @Binds
    abstract fun bindUsuariosPreferences(preferencesImpl: UsuariosPreferencesImpl): UsuariosPreferences
}