package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.notesapp.data.AppDatabase
import com.example.notesapp.ui.navigation.MainScreen
import com.example.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let Compose draw behind the system bars; screens handle their own insets.
        enableEdgeToEdge()

        setContent {
            NotesAppTheme {
                val database = remember { AppDatabase.getDatabase(applicationContext) }
                val noteDao = remember { database.noteDao() }

                MainScreen(noteDao = noteDao)
            }
        }
    }
}
