package com.carnot.leadgeneration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carnot.leadgeneration.api.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor( private val authService: AuthService) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String,context: Context) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Main) {
            // Make network call in background thread
            try {
                val response = withContext(Dispatchers.IO) {
                    authService.login("Bearer patLPaA1sj2uEhi3l.d7d6819315b452e0c2ed4882f79d64975a90d1898e655551794657c83adeafa5")
                }
                // Handle response
                for (user in response.records){
                    if (user.fields.userName == username && user.fields.password == password)
                    {
                        _isLoggedIn.value = true
                        _isLoading.value = false
                        SharedPreferencesUtil.setUsername(context, user.fields.userID)
                        return@launch
                    }
                }
                _isLoggedIn.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _isLoading.value = false
            }

        }
    }

    // Define other authentication functions such as register, logout, etc.
}
