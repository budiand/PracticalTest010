package ro.pub.cs.systems.eim.lab03.practicaltest01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity : AppCompatActivity() {

    private lateinit var numberOfClicksTextView: TextView
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)

        numberOfClicksTextView = findViewById(R.id.number_of_clicks_text_view)
        okButton = findViewById(R.id.ok_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Preluăm numărul de click-uri din Intent
        val numberOfClicks = intent.getIntExtra("number_of_clicks", 0)

        // Setăm textul în TextView
        numberOfClicksTextView.text = "Total button presses: $numberOfClicks"

        // Setăm ascultătorii pentru butoane
        okButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", "OK")
            setResult(RESULT_OK, resultIntent)  // Trimite rezultatul înapoi către activitatea principală
            finish()  // Închide activitatea secundară și revine la activitatea principală
        }

        cancelButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", "Cancel")
            setResult(RESULT_CANCELED, resultIntent)  // Trimite rezultatul înapoi ca anulare
            finish()  // Închide activitatea secundară și revine la activitatea principală
        }
    }
}