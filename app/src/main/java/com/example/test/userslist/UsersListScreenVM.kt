package com.example.test.userslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.core.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ViewState {
    data object Idle : ViewState()
    data object Loading : ViewState()
    data class Success(val users: List<SimpleUser>) : ViewState()
    data class Error(val error: String) : ViewState()
}

@HiltViewModel
internal class UsersListScreenVM @Inject constructor(private val repository: UsersRepository) : ViewModel() {

    var state: ViewState by mutableStateOf(ViewState.Idle)
        private set

    init {
        getUsers()
    }

    private fun getUsers() {
        state = ViewState.Loading
        viewModelScope.launch {
            state = repository.getUsers()?.let { users ->
                ViewState.Success(users.map { SimpleUser(it.id, it.name, it.email, it.website) })
            } ?: ViewState.Error("Error")
        }
    }

}

data class SimpleUser(val id: Int, val name: String, val email: String, val websiteUrl: String)