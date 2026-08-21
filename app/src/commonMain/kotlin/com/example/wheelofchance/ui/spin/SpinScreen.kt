package com.example.wheelofchance.ui.spin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import com.example.wheelofchance.util.toColor
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wheelofchance.data.local.Entry
import com.example.wheelofchance.data.local.Wheel
import com.example.wheelofchance.ui.SpinViewModel
import com.example.wheelofchance.ui.theme.WheelOfChanceTheme
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinScreen(
    viewModel: SpinViewModel,
    onBack: () -> Unit
) {
    val wheel by viewModel.wheel.collectAsState()
    val entries by viewModel.entries.collectAsState()

    SpinContent(
        wheel = wheel,
        entries = entries,
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinContent(
    wheel: Wheel?,
    entries: List<Entry>,
    onBack: () -> Unit
) {
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<Entry?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(wheel?.name ?: "Spin") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (entries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Add some entries first!", style = MaterialTheme.typography.titleLarge)
                }
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                
                Box(
                    modifier = Modifier
                        .size(320.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WheelView(
                        entries = entries,
                        rotation = rotation.value,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    Indicator(modifier = Modifier.align(Alignment.TopCenter))
                }

                Spacer(modifier = Modifier.height(48.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (result != null) "The winner is..." else "Ready to spin?",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = result?.text ?: "???",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        scope.launch {
                            isSpinning = true
                            result = null
                            val extraRotations = 8 + Random.nextInt(5)
                            val targetRotation = rotation.value + extraRotations * 360f + Random.nextInt(360)
                            
                            rotation.animateTo(
                                targetValue = targetRotation,
                                animationSpec = tween(
                                    durationMillis = 4000,
                                    easing = FastOutSlowInEasing
                                )
                            )
                            
                            val normalizedRotation = (rotation.value % 360f + 360f) % 360f
                            val sweepAngle = 360f / entries.size
                            
                            var indicatorAngle = (270f - normalizedRotation) % 360f
                            if (indicatorAngle < 0) indicatorAngle += 360f
                            
                            val index = (indicatorAngle / sweepAngle).toInt() % entries.size
                            result = entries[index]
                            isSpinning = false
                        }
                    },
                    enabled = !isSpinning && entries.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        if (isSpinning) "Spinning..." else "Spin!",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun WheelView(
    entries: List<Entry>,
    rotation: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val size = size.minDimension
        val radius = size / 2
        val sweepAngle = 360f / entries.size

        rotate(rotation) {
            entries.forEachIndexed { index, entry ->
                val startAngle = index * sweepAngle
                drawArc(
                    color = entry.color.toColor(),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size, size),
                    topLeft = Offset((canvasWidth - size) / 2, (canvasHeight - size) / 2)
                )

                // Draw text in the middle of the segment
                val angleRad = (startAngle + sweepAngle / 2) * PI / 180f
                val textRadius = radius * 0.7f
                val x = (canvasWidth / 2) + cos(angleRad).toFloat() * textRadius
                val y = (canvasHeight / 2) + sin(angleRad).toFloat() * textRadius

                val textLayoutResult = textMeasurer.measure(
                    text = entry.text,
                    style = TextStyle(color = Color.White, fontSize = 14.sp)
                )

                rotate(degrees = (startAngle + sweepAngle / 2), pivot = Offset(x, y)) {
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(x - textLayoutResult.size.width / 2, y - textLayoutResult.size.height / 2)
                    )
                }
            }
        }
        
        // Draw center pin
        drawCircle(
            color = Color.White,
            radius = 10.dp.toPx(),
            center = Offset(canvasWidth / 2, canvasHeight / 2)
        )
        drawCircle(
            color = Color.Black,
            radius = 10.dp.toPx(),
            center = Offset(canvasWidth / 2, canvasHeight / 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
        
        // Draw outer border
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = Offset(canvasWidth / 2, canvasHeight / 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
fun Indicator(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val path = Path().apply {
            moveTo(size.width / 2, size.height)
            lineTo(0f, 0f)
            lineTo(size.width, 0f)
            close()
        }
        drawPath(path, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun SpinPreview() {
    WheelOfChanceTheme {
        SpinContent(
            wheel = Wheel(name = "Lunch"),
            entries = listOf(
                Entry(text = "Pizza", color = "#F44336", wheelId = 1),
                Entry(text = "Sushi", color = "#2196F3", wheelId = 1),
                Entry(text = "Burgers", color = "#4CAF50", wheelId = 1),
                Entry(text = "Salad", color = "#FFEB3B", wheelId = 1)
            ),
            onBack = {}
        )
    }
}
