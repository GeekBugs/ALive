package com.geekbug.alive

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

/**
 * 保活进程设置
 *
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class KeepAliveJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "JobService onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 如果当前设备大于 7.0,延迟 5 秒，再次执行一次
            startJob(this)
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "JobService onStopJob")
        return false
    }

    companion object {

        private const val TAG = "KeepAliveJobService"

        private const val TIME = 15_000L

        fun startJob(context: Context) {
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfoBuilder = JobInfo.Builder(
                10,
                ComponentName(context.packageName, KeepAliveJobService::class.java.name)
            )
            // 7.0 以下，可以每隔 15 秒执行一次任务
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                jobInfoBuilder.setPeriodic(TIME)
            } else {
                // 7.0 以上，设置延迟 15 秒执行
                jobInfoBuilder.setMinimumLatency(TIME)
            }
            // 开启定时任务
            jobScheduler.schedule(jobInfoBuilder.build())
        }
    }
}
