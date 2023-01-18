package com.lord_ukaka.worldnews.domain.usecases

import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.domain.models.User
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUsecase @Inject constructor(
    private val repository: NewsRepository
    ) {

    suspend fun signInUser(email: String?, password: String?) : DataResponse<User?> {
        return repository.signInUser(email, password)
    }

    fun getAllUsers(): Flow<List<User>> {
        return repository.getAllUsers()
    }

    suspend fun getUser(id: Int): User? {
        return repository.getUser(id)
    }

    suspend fun insertUser(user: User) {
        return repository.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        return repository.deleteUser(user)
    }
}