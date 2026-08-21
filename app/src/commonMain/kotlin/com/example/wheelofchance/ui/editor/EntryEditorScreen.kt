package com.example.wheelofchance.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.wheelofchance.util.toColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wheelofchance.data.local.Entry
import com.example.wheelofchance.data.local.Wheel
import com.example.wheelofchance.ui.EditorViewModel
import com.example.wheelofchance.ui.theme.WheelOfChanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryEditorContent(
    wheel: Wheel?,
    entries: List<Entry>,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    onAddEntry: (String, String) -> Unit,
    onDeleteEntry: (Entry) -> Unit,
    modifier: Modifier = Modifier
) {
    var wheelName by remember { mutableStateOf("") }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(wheel) {
        wheel?.let {
            wheelName = it.name
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Edit Wheel") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        onSave(wheelName)
                        onBack()
                    }) {
                        Icon(Icons.Rounded.Done, contentDescription = "Save")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddEntryDialog = true },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Entry")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = wheelName,
                onValueChange = { wheelName = it },
                label = { Text("Wheel Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = "Entries",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    EntryListItem(
                        entry = entry,
                        onDelete = { onDeleteEntry(entry) }
                    )
                }
            }
        }

        if (showAddEntryDialog) {
            AddEntryDialog(
                onDismiss = { showAddEntryDialog = false },
                onConfirm = { text, color ->
                    onAddEntry(text, color)
                    showAddEntryDialog = false
                }
            )
        }
    }
}

@Composable
fun EntryEditorScreen(
    viewModel: EditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wheel by viewModel.wheel.collectAsState()
    val entries by viewModel.entries.collectAsState()
    
    EntryEditorContent(
        wheel = wheel,
        entries = entries,
        onBack = onBack,
        onSave = { viewModel.updateWheelName(it) },
        onAddEntry = { text, color -> viewModel.addEntry(text, color) },
        onDeleteEntry = { viewModel.deleteEntry(it) },
        modifier = modifier
    )
}

@Composable
fun EntryListItem(
    entry: Entry,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(entry.color.toColor())
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = entry.text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Rounded.Delete, contentDescription = "Delete Entry")
        }
    }
}

@Composable
fun AddEntryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#FF0000") }

    val colors = listOf(
        "#F44336", "#E91E63", "#9C27B0", "#673AB7",
        "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
        "#009688", "#4CAF50", "#8BC34A", "#CDDC39",
        "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Entry") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Entry Text") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text("Select Color", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Show a few colors or a scrollable row
                    Column {
                        colors.chunked(4).forEach { rowColors ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                                rowColors.forEach { colorStr ->
                                    val color = colorStr.toColor()
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .border(
                                                width = if (selectedColor == colorStr) 2.dp else 0.dp,
                                                color = if (selectedColor == colorStr) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable { selectedColor = colorStr }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (text.isNotBlank()) onConfirm(text, selectedColor) },
                enabled = text.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EntryEditorPreview() {
    WheelOfChanceTheme {
        EntryEditorContent(
            wheel = Wheel(id = 1, name = "Dinner Options"),
            entries = listOf(
                Entry(id = 1, wheelId = 1, text = "Pizza", color = "#F44336"),
                Entry(id = 2, wheelId = 1, text = "Sushi", color = "#2196F3"),
                Entry(id = 3, wheelId = 1, text = "Burgers", color = "#4CAF50")
            ),
            onBack = {},
            onSave = {},
            onAddEntry = { _, _ -> },
            onDeleteEntry = {}
        )
    }
}
