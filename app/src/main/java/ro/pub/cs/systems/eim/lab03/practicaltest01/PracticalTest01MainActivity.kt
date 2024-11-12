package ro.pub.cs.systems.eim.lab03.practicaltest01

import ro.pub.cs.systems.eim.lab03.practicaltest01.general.Constants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ro.pub.cs.systems.eim.lab03.practicaltest01.general.Constants.INPUT1
import ro.pub.cs.systems.eim.lab03.practicaltest01.general.Constants.INPUT2

class PracticalTest01MainActivity : AppCompatActivity() {

    // D
    private val intentFilter = IntentFilter()

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                Log.d(Constants.BROADCAST_RECEIVER_TAG, it.action.toString())
                Log.d(Constants.BROADCAST_RECEIVER_TAG, it.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA).toString())
            }
        }
    }

    private lateinit var leftEditText: EditText
    private lateinit var rightEditText: EditText
    private lateinit var navigateToSecondaryActivityButton: Button

    // B2.b
    // Chei pentru salvarea și restaurarea valorilor
    private val LEFT_VALUE_KEY = "leftValue"
    private val RIGHT_VALUE_KEY = "rightValue"

    // C1
    private var leftClicks = 0
    private var rightClicks = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)

        // B.1
        leftEditText = findViewById(R.id.left_edit_text)
        rightEditText = findViewById(R.id.right_edit_text)

        navigateToSecondaryActivityButton = findViewById(R.id.navigate_to_secondary_activity_button)


        val pressMeButton = findViewById<Button>(R.id.left_button)
        pressMeButton.setOnClickListener {
            leftClicks++
            leftEditText.setText(leftClicks.toString())
            startServiceIfConditionIsMet(leftClicks, rightClicks)
        }

        val pressMeToo = findViewById<Button>(R.id.right_button)
        pressMeToo.setOnClickListener {
            rightClicks++
            rightEditText.setText(rightClicks.toString())
            startServiceIfConditionIsMet(leftClicks, rightClicks)
        }

        val startServiceButton = findViewById<Button>(R.id.start_service_button)
        startServiceButton.setOnClickListener {
            stopService(Intent(this, PracticalTest01Service::class.java))
            Log.d(Constants.BROADCAST_RECEIVER_TAG, "Service Stopped!")
        }

        Constants.actionTypes.forEach { action ->
            intentFilter.addAction(action)
        }

        // C1
        navigateToSecondaryActivityButton.setOnClickListener {
            // Lansează activitatea secundară cu numărul total de clickuri
            val totalClicks = leftClicks + rightClicks
            val intent = Intent(this, PracticalTest01SecondaryActivity::class.java)
            intent.putExtra("number_of_clicks", totalClicks)
            startActivityForResult(intent, 1);  // Lansează activitatea secundară
        }
    }

    private fun startServiceIfConditionIsMet(leftNumber: Int, rightNumber: Int) {
        if (leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THRESHOLD) {
            val intent = Intent(applicationContext, PracticalTest01Service::class.java).apply {
                putExtra(INPUT1, leftNumber)
                putExtra(INPUT2, rightNumber)
            }
            applicationContext.startService(intent)
        }
    }

    // C1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("result")
                //Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Rezultatul primit: $result", Toast.LENGTH_LONG).show()
                // Poți prelucra rezultatul (ex: afișa un mesaj sau actualiza UI)
            } else if (resultCode == RESULT_CANCELED) {
                val result = data?.getStringExtra("result")
                //Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Rezultatul primit: $result", Toast.LENGTH_LONG).show()
                // Poți prelucra rezultatul (ex: afișa un mesaj sau actualiza UI)
            }
        }
    }

    // Restaurăm valorile în onRestoreInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        leftEditText.setText(savedInstanceState.getString(LEFT_VALUE_KEY, "0"))
        rightEditText.setText(savedInstanceState.getString(RIGHT_VALUE_KEY, "0"))
    }

    // Salvăm valorile în Bundle înainte ca activitatea să fie distrusă
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(LEFT_VALUE_KEY, leftEditText.text.toString())
        outState.putString(RIGHT_VALUE_KEY, rightEditText.text.toString())
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(messageBroadcastReceiver)
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, PracticalTest01Service::class.java)
        applicationContext.stopService(intent)
        super.onDestroy()
    }


}