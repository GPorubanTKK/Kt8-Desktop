package pages

import Compiler
import Processor
import WriteTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState.Companion.state
import components.VerticalSpacer
import components.rememberMutableStateOf
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.math.min

@Composable fun Executor(
    debug: Boolean,
    setDebug: (Boolean) -> Unit
) = Column(modifier = Modifier.fillMaxSize()) {
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    var path by rememberMutableStateOf(System.getProperty("user.home") + "\\Desktop\\or.gasm")
    var content by rememberMutableStateOf("")
    var consoleText by rememberMutableStateOf("")
    var programLoaded by rememberMutableStateOf(false)
    var programLength by rememberMutableStateOf(0u)
    var programCounter by rememberMutableStateOf(state.programMemoryStart)
    SideEffect {
        try { state.processor.toString() } catch(e: Exception) {
            println("initializing processor")
            state.processor = Processor(
                state.ram,
                stackRange = (state.stackStart..(state.stackStart+state.stackSize)),
                programMemory = (state.programMemory..(state.programMemoryStart+state.programMemory)),
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
            val compiledGasm = Compiler().compileGasm(content)
            state.ram.clear()
            state.ram.load(compiledGasm, state.programMemoryStart.toInt())
            programLoaded = true
            programLength = (compiledGasm.size - 3).toUInt()
        }, enabled = !programLoaded) { Text("Compile") }
        HorizontalSpacer(10.dp)
        Button({
            programLoaded = false
            state.processor.execute(state.programMemoryStart) { pc -> programCounter == pc }
        }, enabled = programLoaded) { Text("Run") }
        HorizontalSpacer(10.dp)
        Button({ setDebug(!debug) }, enabled = programLoaded) { Text("Debug") }
        HorizontalSpacer(10.dp)
        Button({ programCounter = min(programLength, programCounter + 3u) }, enabled = programLoaded && debug) { Icon(Icons.Filled.FastForward, "") }
    }
    VerticalSpacer(12.dp)
    TextField(content, {content = it}, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(consoleText, {}, readOnly = true, modifier = Modifier.fillMaxSize())
}