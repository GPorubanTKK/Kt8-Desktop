package pages

import Compiler
import Processor
import WriteTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState.Companion.state
import components.VerticalSpacer
import components.rememberMutableStateOf
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@Composable fun Executor() = Column(modifier = Modifier.fillMaxSize()) {
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    var path by rememberMutableStateOf(System.getProperty("user.home") + "\\Desktop\\or.gasm")
    var content by rememberMutableStateOf("")
    var consoleText by rememberMutableStateOf("")
    SideEffect {
        try {
            state.processor.toString()
        } catch(e: Exception) {
            println(e)
            println("initializing processor")
            state.processor = Processor(
                state.ram,
                stackRange = (state.stackStart.toInt()..(state.stackStart+state.stackSize).toInt()),
                programMemory = (state.programMemory.toInt()..(state.programMemoryStart+state.programMemory).toInt()),
                outputStream = object : WriteTarget { override fun print(str: String) { consoleText += str } }
            )
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(path, {path = it})
        HorizontalSpacer(10.dp)
        Button({
            content = try {
                BufferedReader(FileReader(File(path))).readText()
            } catch(e: Exception) { "${e.message}: ${e.cause}" }
        }) { Text("Load") }
        HorizontalSpacer(10.dp)
        Button({
            state.ram.clear()
            state.ram.load(Compiler().compileGasm(content), state.programMemoryStart.toInt())
            state.processor.execute(state.programMemoryStart.toInt())
        }) { Text("Run") }
        HorizontalSpacer(10.dp)
    }
    VerticalSpacer(12.dp)
    TextField(content, {content = it}, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(consoleText, {}, readOnly = false, modifier = Modifier.fillMaxSize())
}