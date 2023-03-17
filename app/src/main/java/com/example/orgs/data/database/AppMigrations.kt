package com.example.orgs.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Cria tabela Usuario
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Usuario` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `usuario` TEXT NOT NULL,
                `nome` TEXT NOT NULL,
                `senha` TEXT NOT NULL
            )
        """
        )
    }
}

// Adiciona foreign key para tabela Produto
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
               ALTER TABLE `Produto`
               RENAME TO `ProdutoOld`
            """
        )

        database.execSQL(
            """
               CREATE TABLE IF NOT EXISTS `Produto` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `titulo` TEXT NOT NULL,
                `descricao` TEXT NOT NULL,
                `valor` REAL NOT NULL,
                `imagemUrl` TEXT,
                `usuarioId` INTEGER,
                FOREIGN KEY(`usuarioId`) REFERENCES `Usuario`(`id`)
                ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """
        )

        database.execSQL(
            """
                INSERT INTO `Produto` (id, titulo, descricao, valor, imagemUrl, usuarioId)
                SELECT id, titulo, descricao, valor, imagemUrl, usuarioId
                FROM `ProdutoOld`
            """
        )

        database.execSQL(
            """
                DROP TABLE `ProdutoOld`
            """
        )
    }
}

// Adiciona índice para campo usuarioId na tabela Produto
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE INDEX index_Produto_usuarioId ON Produto(usuarioId)
            """
        )
    }
}
