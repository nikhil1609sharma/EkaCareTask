package com.ekacare.ekacaretask.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.*
import com.ekacare.ekacaretask.Model.User
import com.ekacare.ekacaretask.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val allUsers: LiveData<List<User>> = repository.allUsers
    private val _exception = MutableLiveData<String>()
    val exception: LiveData<String> get() = _exception
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isException = MutableStateFlow(false)
    val isException: StateFlow<Boolean> = _isException
    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> get() = _saveSuccess


    fun insert(user: User) = viewModelScope.launch {
        _isLoading.value = true
        try {
            repository.insert(user)
            _saveSuccess.value = true
        }catch (e: Exception) {
            _isException.value = true
            _exception.value = e.message
            _saveSuccess.value = false
        }
        finally {
            _isLoading.value = false
        }
    }
}


