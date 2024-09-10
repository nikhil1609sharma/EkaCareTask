package com.ekacare.ekacaretask.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekacare.ekacaretask.Model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>
}
