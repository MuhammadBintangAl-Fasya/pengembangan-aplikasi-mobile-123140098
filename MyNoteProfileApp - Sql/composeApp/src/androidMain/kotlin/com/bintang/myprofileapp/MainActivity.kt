package com.bintang.myprofileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bintang.myprofileapp.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Buat DatabaseDriverFactory dengan Android Context
        val driverFactory = DatabaseDriverFactory(applicationContext)

        setContent {
            App(driverFactory = driverFactory)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview menggunakan DatabaseDriverFactory juga
    // Catatan: preview mungkin tidak bisa berjalan karena butuh Context asli
    // App(driverFactory = DatabaseDriverFactory(LocalContext.current))
}