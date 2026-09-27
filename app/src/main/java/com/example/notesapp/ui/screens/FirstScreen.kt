package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDao
import kotlinx.coroutines.launch

/**
 * Form for creating a new [Note]. No Scaffold here — padding/insets are
 * provided by the top-level Scaffold in MainScreen.
 */
@Composable
fun FirstScreen(
    noteDao: NoteDao,
    snackbarHostState: SnackbarHostState
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "New Note",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            minLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val trimmedTitle = title.trim()
                val trimmedContent = content.trim()

                if (trimmedTitle.isEmpty() || trimmedContent.isEmpty()) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Title and content cannot be empty")
                    }
                    return@Button
                }

                coroutineScope.launch {
                    noteDao.insertNote(Note(title = trimmedTitle, content = trimmedContent))
                    title = ""
                    content = ""
                    snackbarHostState.showSnackbar("Note saved")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Note")
        }
    }
}
