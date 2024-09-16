package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import components.*
import components.SharedState.Companion.STATE

@Composable fun Settings() = Column(modifier = maxSize) {
    @Composable fun SettingsRow(textBefore: String, textAfter: String, text: String, handle: (Int) -> Unit) {
        Row(modifier = maxWidth, verticalAlignment = Alignment.CenterVertically) {
            HorizontalSpacer(2.dp)
            Text("$textBefore ")
            HorizontalSpacer(1.dp)
            NumTextField(text, 65536, 0, onUpdate = handle)
            Text(" $textAfter")
        }
    }

    var memory by remember { mutableStateOf(STATE.memory) }
    var stackSize by remember { mutableStateOf(STATE.stackSize) }
    var stackStart by remember { mutableStateOf(STATE.stackStart) }
    var programSize by remember { mutableStateOf(STATE.programMemory) }
    var programStart by remember { mutableStateOf(STATE.programMemoryStart) }

    VerticalSpacer(5.dp)
    SettingsRow("System Memory (max 64kb)", "bytes", "$memory") { memory = it.toUShort() }
    VerticalSpacer(5.dp)
    SettingsRow("Stack size (max 255b)", "bytes", "$stackSize") { stackSize = it.toUByte() }
    VerticalSpacer(5.dp)
    SettingsRow("Stack start byte", "", "$stackStart") { stackStart = it.toUShort() }
    VerticalSpacer(5.dp)
    SettingsRow("Size of program memory", "bytes", "$programSize") { programSize = it.toUShort() }
    VerticalSpacer(5.dp)
    SettingsRow("Start of program memory", "", "$programStart") { programStart = it.toUShort() }
    VerticalSpacer(10.dp)
    Button({
        STATE.memory = memory
        STATE.stackSize = stackSize
        STATE.stackStart = stackStart
        STATE.programMemory = programSize
        STATE.programMemoryStart = programStart
        STATE.updateSettings()
    }) { Text("Apply Settings") }
}