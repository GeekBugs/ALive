package com.geekbug.alive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

/**
 * 远程提权前台服务
 *
 */
class RemoteForegroundService : Service() {

    private var mChatBinder: ChatBinder? = null
    private var mConnection: Connection? = null

    class ChatBinder : IMyAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            // ignore
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mChatBinder
    }

    override fun onCreate() {
        super.onCreate()
        mChatBinder = ChatBinder()
        startService()
    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("service", "service", NotificationManager.IMPORTANCE_NONE)
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(this, "service")
            val notification = builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            startForeground(10, notification)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(10, Notification())
            startService(Intent(this, CancelNotificationService::class.java))
        }
    }

    private fun bindService() {
        mConnection = Connection()
        val bindIntent = Intent(this, LocalForegroundService::class.java)
        mConnection?.let {
            bindService(bindIntent, it, BIND_AUTO_CREATE)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindService()
        return super.onStartCommand(intent, flags, startId)
    }

    inner class Connection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            startService()
            bindService()
        }
    }

    class CancelNotificationService : Service() {

        override fun onCreate() {
            super.onCreate()
            startForeground(10, Notification())
            stopSelf()
        }

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }
    }
}
