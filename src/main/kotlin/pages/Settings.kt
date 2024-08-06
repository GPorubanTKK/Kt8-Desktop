package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState
import components.VerticalSpacer

@Composable fun Settings() = Column(modifier = Modifier.fillMaxSize()) {
    var memory by remember { mutableStateOf(SharedState.state.memory) }
    var stackSize by remember { mutableStateOf(SharedState.state.stackSize) }
    var stackStart by remember { mutableStateOf(SharedState.state.stackStart) }
    var programSize by remember { mutableStateOf(SharedState.state.programMemory) }
    var programStart by remember { mutableStateOf(SharedState.state.programMemoryStart) }

    VerticalSpacer(5.dp)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalSpacer(2.dp)
        Text("Max system memory (max 64kb) ")
        HorizontalSpacer(1.dp)
        TextField("$memory", { if(it.matches("^\\d+$".toRegex()) && it.toUInt() <= 65536u) memory = it.toUInt() })
        Text(" bytes")
    }
    VerticalSpacer(5.dp)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalSpacer(2.dp)
        Text("Stack size ")
        HorizontalSpacer(1.dp)
        TextField("$stackSize", { if(it.matches("^\\d+$".toRegex()) && it.toUInt() <= 65536u) stackSize = it.toUInt() })
        Text(" bytes")
    }
    VerticalSpacer(5.dp)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalSpacer(2.dp)
        Text("Stack start position ")
        HorizontalSpacer(1.dp)
        TextField("$stackStart", { if(it.matches("^\\d+$".toRegex()) && it.toUInt() <= 65536u) stackStart = it.toUInt() })
    }
    VerticalSpacer(5.dp)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalSpacer(2.dp)
        Text("Program memory size ")
        HorizontalSpacer(1.dp)
        TextField("$programSize", { if(it.matches("^\\d+$".toRegex()) && it.toUInt() <= 65536u) programSize = it.toUInt() })
        Text(" bytes")
    }
    VerticalSpacer(5.dp)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalSpacer(2.dp)
        Text("Program memory start position ")
        HorizontalSpacer(1.dp)
        TextField("$programStart", { if(it.matches("^\\d+$".toRegex()) && it.toUInt() <= 65536u) programStart = it.toUInt() })
    }
    VerticalSpacer(10.dp)
    Button({
        SharedState.state.memory = memory
        SharedState.state.stackSize = stackSize
        SharedState.state.stackStart = stackStart
        SharedState.state.programMemory = programSize
        SharedState.state.programMemoryStart = programStart
        SharedState.state.updateSettings()
    }) { Text("Apply Settings") }
}