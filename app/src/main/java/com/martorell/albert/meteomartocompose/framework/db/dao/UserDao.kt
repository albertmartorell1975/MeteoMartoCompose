package com.martorell.albert.meteomartocompose.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.martorell.albert.meteomartocompose.framework.db.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query(value = "SELECT * FROM User WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>

    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>>

    @Update
    suspend fun updateUser(user: User)

}