package com.example.gym_tok.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gym_tok.dao.UsuarioLocalDAO
import com.example.gym_tok.model.UsuarioLocal

@Database(
    entities = [UsuarioLocal::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioLocalDAO(): UsuarioLocalDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym-tok.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}