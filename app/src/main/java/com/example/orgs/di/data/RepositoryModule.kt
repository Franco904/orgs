package com.example.orgs.di.data

import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.data.database.repositories.ProdutosRepositoryImpl
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProdutosRepository(repository: ProdutosRepositoryImpl): ProdutosRepository

    @Binds
    abstract fun bindUsuariosRepository(repository: UsuariosRepositoryImpl): UsuariosRepository
}