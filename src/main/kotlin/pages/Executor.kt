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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState.Companion.state
import components.VerticalSpacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.math.min

@Composable fun Executor(
    debug: Boolean,
    setDebug: (Boolean) -> Unit,
    executorState: SnapshotStateMap<String, Any>,
    setMu: (Int) -> Unit
) = Column(modifier = Modifier.fillMaxSize()) {
    val crScope = rememberCoroutineScope { Dispatchers.IO }
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    SideEffect {
        try { state.processor.toString() } catch(e: Exception) {
            state.processor = Processor(
                state.ram,
                stackRange = (state.stackStart..(state.stackStart+state.stackSize)),
                programMemory = (state.programMemory..(state.programMemoryStart+state.programMemory)),
                outputStream = object : WriteTarget { override fun print(str: String) {
                    executorState["consoleText"] = executorState["consoleText"]!! as String + str
                } },
                onMemoryUpdate = { pc ->
                    println("Updating fn: $pc")
                    setMu(pc)
                },
            )
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(executorState["path"]!! as String, { updated -> executorState["path"] = updated })
        HorizontalSpacer(10.dp)
        Button({
            executorState["content"] = try {
                BufferedReader(FileReader(File(executorState["path"] as String))).readText()
            } catch(e: Exception) { "${e.message}: ${e.cause}" }
        }) { Text("Load") }
        HorizontalSpacer(10.dp)
        Button({
            try {
                val compiledGasm = Compiler().compileGasm(executorState["content"]!! as String)
                println("Compiled gASM ${compiledGasm.toList()}")
                state.processor.reset()
                state.ram.load(compiledGasm, state.programReadStart.toInt())
                executorState["programLoaded"] = true
                executorState["programLength"] = (compiledGasm.size - 3).toUInt()
                executorState["programCounter"] = state.programReadStart + 3u
            } catch(e: Exception) { println("Error: ${e.message ?: "Unknown Error"}") }
        }) { Text("Compile") }
        HorizontalSpacer(10.dp)
        Button({
            executorState["programLoaded"] = false
            executorState["consoleText"] = ""
            crScope.launch {
                try {
                    state.processor.execute(state.programReadStart.toUShort())
                } catch(e: Exception) { executorState["consoleText"] = "Error: ${e.message ?: "Unknown Error"}" }
            }
            setDebug(false)
        }) { Text("Run") }
        HorizontalSpacer(10.dp)
        Button({
            setDebug(true)
            crScope.launch {
                try {
                    state.processor.execute(state.programReadStart.toUShort()) { pc -> ((executorState["programCounter"] as UInt).toUShort() == pc) }
                } catch(e: Exception) { executorState["consoleText"] = e.message ?: "Unknown Error" }
                setDebug(false)
            }
        }) { Text("Debug") }
        HorizontalSpacer(10.dp)
        Button({
            executorState["programCounter"] = min(executorState["programLength"] as UInt + executorState["programCounter"] as UInt, executorState["programCounter"] as UInt + 3u)
        }, enabled = debug) { Icon(Icons.Filled.FastForward, "") }
    }
    VerticalSpacer(12.dp)
    TextField(executorState["content"]!! as String, { u -> executorState["content"] = u}, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(executorState["consoleText"]!! as String, {}, readOnly = true, modifier = Modifier.fillMaxSize())
}