package com.ekacare.ekacaretask.Repository

import androidx.lifecycle.LiveData
import com.ekacare.ekacaretask.Database.UserDao
import com.ekacare.ekacaretask.Model.User

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}
