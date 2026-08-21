package com.example.wheelofchance.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wheelofchance.data.local.Wheel
import com.example.wheelofchance.ui.WheelViewModel
import com.example.wheelofchance.ui.theme.WheelOfChanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    wheels: List<Wheel>,
    onAddConfirm: (String) -> Unit,
    onWheelClick: (Long) -> Unit,
    onSpinClick: (Long) -> Unit,
    onDeleteWheel: (Wheel) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Wheel of Chance") },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add Wheel",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    ) { innerPadding ->
        if (wheels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create your first wheel to get started!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 88.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(wheels, key = { it.id }) { wheel ->
                    WheelListItem(
                        wheel = wheel,
                        onClick = { onWheelClick(wheel.id) },
                        onSpinClick = { onSpinClick(wheel.id) },
                        onDelete = { onDeleteWheel(wheel) }
                    )
                }
            }
        }

        if (showAddDialog) {
            AddWheelDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name ->
                    onAddConfirm(name)
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: WheelViewModel,
    onWheelClick: (Long) -> Unit,
    onSpinClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val wheels by viewModel.allWheels.collectAsState()
    DashboardContent(
        wheels = wheels,
        onAddConfirm = { viewModel.addWheel(it) },
        onWheelClick = onWheelClick,
        onSpinClick = onSpinClick,
        onDeleteWheel = { viewModel.deleteWheel(it) },
        modifier = modifier
    )
}

@Composable
fun WheelListItem(
    wheel: Wheel,
    onClick: () -> Unit,
    onSpinClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        ListItem(
            headlineContent = {
                Text(
                    wheel.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            colors = androidx.compose.material3.ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onSpinClick,
                        colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Spin Wheel"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete Wheel",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun AddWheelDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Wheel") },
        text = {
            Column {
                Text("Enter a name for your wheel:")
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.padding(top = 8.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
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
fun DashboardPreview() {
    WheelOfChanceTheme {
        DashboardContent(
            wheels = listOf(
                Wheel(id = 1, name = "Dinner Options"),
                Wheel(id = 2, name = "Movie Night"),
                Wheel(id = 3, name = "Who pays?")
            ),
            onAddConfirm = {},
            onWheelClick = {},
            onSpinClick = {},
            onDeleteWheel = {}
        )
    }
}
