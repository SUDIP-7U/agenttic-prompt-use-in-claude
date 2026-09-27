package com.example.notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single note.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String
)
