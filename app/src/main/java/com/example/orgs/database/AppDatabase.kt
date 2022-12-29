package com.example.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.orgs.database.converters.Converters
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.model.Produto

@Database(
    version = AppDatabase.SCHEMA_VERSION,
    entities = [Produto::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val SCHEMA_VERSION = 1

        // Mudanças ficam visíveis para todas as threads, evitando inconsistências
        @Volatile
        private lateinit var database: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (::database.isInitialized) return database

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db",
            )
                .allowMainThreadQueries()
                .build().also {
                    database = it
                }
        }
    }

    abstract fun produtosDao(): ProdutosDao
}