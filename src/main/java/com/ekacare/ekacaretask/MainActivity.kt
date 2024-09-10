package com.ekacare.ekacaretask

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.ekacare.ekacaretask.Database.UserDatabase
import com.ekacare.ekacaretask.Repository.UserRepository
import com.ekacare.ekacaretask.Screens.UserForm
import com.ekacare.ekacaretask.Screens.UserScreen
import com.ekacare.ekacaretask.ViewModel.SplashViewModel
import com.ekacare.ekacaretask.ViewModel.UserViewModel
import com.ekacare.ekacaretask.ViewModel.UserViewModelFactory
import com.ekacare.ekacaretask.ui.theme.EkaCareTaskTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val userDao = UserDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        splashScreen.setKeepOnScreenCondition{viewModel.isLoading.value}

        setContent {
            UserScreen(viewModel = userViewModel)

        }

    }
}
