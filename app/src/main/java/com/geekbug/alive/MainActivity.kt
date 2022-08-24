package com.geekbug.alive

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        ContextCompat.startForegroundService(
            this,
            Intent(this, LocalForegroundService::class.java)
        )
        ContextCompat.startForegroundService(
            this,
            Intent(this, RemoteForegroundService::class.java)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            KeepAliveJobService.startJob(this)
        }
    }
}
