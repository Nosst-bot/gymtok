package com.example.gym_tok.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gym_tok.model.UsuarioLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioLocalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioLocal)

    @Query("SELECT * FROM usuarioLocal WHERE email = :email")
    suspend fun getUserByEmail(email: String): UsuarioLocal?

    @Query("SELECT * FROM usuarioLocal WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UsuarioLocal?>

    @Query("UPDATE usuarioLocal SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Query("UPDATE usuarioLocal SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun loginUser(userId: Int)
}
