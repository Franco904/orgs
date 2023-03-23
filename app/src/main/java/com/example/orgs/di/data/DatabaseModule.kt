package com.example.orgs.di.data

import android.content.Context
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.dao.ProdutosDao
import com.example.orgs.data.database.dao.UsuariosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    // Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    // DAOs
    @Provides
    @Singleton
    fun provideProdutosDao(db: AppDatabase): ProdutosDao {
        return db.produtosDao()
    }

    @Provides
    @Singleton
    fun provideUsuariosDao(db: AppDatabase): UsuariosDao {
        return db.usuariosDao()
    }
}