package ro.pub.cs.systems.eim.lab03.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ro.pub.cs.systems.eim.lab03.practicaltest01.general.Constants

class PracticalTest01Service : Service() {

    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Retrieve the numbers from the intent extras
        val input1 = intent.getIntExtra(Constants.INPUT1, 0)
        val input2 = intent.getIntExtra(Constants.INPUT2, 0)

        // Start the processing thread to handle calculations and broadcasts
        processingThread = ProcessingThread(this, input1, input2)
        processingThread?.start()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        // Stop the thread when the service is destroyed
        processingThread?.stopThread()
        super.onDestroy()
    }
}
