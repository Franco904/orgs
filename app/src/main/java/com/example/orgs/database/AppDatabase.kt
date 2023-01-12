package com.example.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.orgs.database.converters.Converters
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.database.dao.UsuariosDao
import com.example.orgs.model.Produto
import com.example.orgs.model.Usuario

@Database(
    version = AppDatabase.SCHEMA_VERSION,
    entities = [
        Produto::class,
        Usuario::class,
    ],
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val SCHEMA_VERSION = 2

        // Mudanças ficam visíveis para todas as threads, evitando inconsistências
        @Volatile
        private lateinit var database: AppDatabase

        private val MIGRATIONS = arrayOf(
            MIGRATION_1_2,
        )

        fun getInstance(context: Context): AppDatabase {
            if (::database.isInitialized) return database

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db",
            )
                .addMigrations(*MIGRATIONS)
                .build().also {
                    database = it
                }
        }
    }

    abstract fun produtosDao(): ProdutosDao

    abstract fun usuariosDao(): UsuariosDao
}