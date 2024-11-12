package ro.pub.cs.systems.eim.lab03.practicaltest01

import android.content.Context
import android.content.Intent
import android.util.Log
import ro.pub.cs.systems.eim.lab03.practicaltest01.general.Constants
import java.util.*
import kotlin.math.sqrt

class ProcessingThread(
    private val context: Context,
    private val input1: Int,
    private val input2: Int
) : Thread() {

    private var isRunning = true

    // Calculate arithmetic and geometric means
    private val arithmeticMean = (input1 + input2) / 2.0
    private val geometricMean = sqrt((input1 * input2).toDouble())

    override fun run() {
        Log.d(Constants.BROADCAST_RECEIVER_TAG, "Thread started! PID: ${android.os.Process.myPid()}, TID: ${android.os.Process.myTid()}")
        while (isRunning) {
            sendMessage()
            sleepThread()
        }
        Log.d(Constants.BROADCAST_RECEIVER_TAG, "Thread stopped!")
    }

    private fun sendMessage() {
        val intent = Intent().apply {
            // Choose one of the actions at random
            action = Constants.actionTypes.random()
            putExtra(
                Constants.BROADCAST_RECEIVER_EXTRA,
                "Time: ${Date()}, Arithmetic Mean: $arithmeticMean, Geometric Mean: $geometricMean"
            )
        }
        context.sendBroadcast(intent)
    }

    private fun sleepThread() {
        try {
            sleep(10000)  // 10 seconds
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun stopThread() {
        isRunning = false
    }
}
