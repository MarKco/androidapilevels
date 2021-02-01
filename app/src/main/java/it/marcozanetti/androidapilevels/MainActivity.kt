package it.marcozanetti.androidapilevels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // For testing purposes
        //val apiLevelsRetriever = APILevelsRetriever()
        //apiLevelsRetriever.retrieveAPILevelsFromWeb()
    }
}