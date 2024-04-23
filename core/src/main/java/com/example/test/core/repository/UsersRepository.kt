package com.example.test.core.repository

import com.example.test.core.webservice.IoDispatcher
import com.example.test.core.webservice.UsersClient
import com.example.test.core.webservice.domain.User
import com.example.test.core.webservice.domain.UsersApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(@UsersClient private val usersApi: UsersApi, @IoDispatcher private val ioDispatcher: CoroutineDispatcher) {

    suspend fun getUsers(): List<User>? = withContext(ioDispatcher) {
        runCatching {
            usersApi.getUsers()
        }.getOrNull()
    }
}