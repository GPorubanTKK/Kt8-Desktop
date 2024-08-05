package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable fun Profiler() = Column(modifier = Modifier.fillMaxSize()) {
    Text("Memory Profiler")
}