package com.hilmi.wartego.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.hilmi.wartego.R
import com.hilmi.wartego.ui.auth.AuthActivity
import com.hilmi.wartego.ui.home.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isLogin.collect { isLoggedIn ->
                isLoggedIn?.let {
                    if (it) {
                        delay(2000)
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                DashboardActivity::class.java
                            )
                        )
                    } else {
                        delay(2000)
                        startActivity(Intent(this@SplashScreenActivity, AuthActivity::class.java))
                    }
                    finish()
                }
            }
        }
    }
}