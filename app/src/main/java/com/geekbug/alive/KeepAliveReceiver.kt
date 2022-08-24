package com.geekbug.alive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import androidx.core.content.ContextCompat

/**
 * 开机启动服务
 *
 */
class KeepAliveReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "KeepAliveReceiver"
        const val CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if (TextUtils.equals(
                    Intent.ACTION_BOOT_COMPLETED,
                    action
                ) || TextUtils.equals(
                        CONNECTIVITY_CHANGE,
                        action
                    )
            ) {
                context?.let {
                    ContextCompat.startForegroundService(
                        context,
                        Intent(context, LocalForegroundService::class.java)
                    )
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context?.let {
                        KeepAliveJobService.startJob(it)
                    }
                }
            }
        }
    }
}
