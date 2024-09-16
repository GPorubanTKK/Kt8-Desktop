package pages

import Compiler
import Processor
import WriteTarget
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState.Companion.STATE
import components.VerticalSpacer
import components.maxSize
import components.maxWidth
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
) = Column(modifier = maxSize) {
    val crScope = rememberCoroutineScope { Dispatchers.IO }
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    SideEffect {
        try { STATE.processor.toString() } catch(e: Exception) {
            STATE.processor = Processor(
                STATE.ram,
                stackRange = (STATE.stackStart..(STATE.stackStart + STATE.stackSize).toUShort()),
                programMemory = (STATE.programMemory..(STATE.programMemoryStart+STATE.programMemory).toUShort()),
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
    Row(modifier = maxWidth) {
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
                STATE.processor.reset()
                STATE.ram.load(compiledGasm, STATE.programReadStart.toInt())
                executorState["programLoaded"] = true
                executorState["programLength"] = (compiledGasm.size - 3).toUInt()
                executorState["programCounter"] = STATE.programReadStart + 3u
            } catch(e: Exception) { println("Error: ${e.message ?: "Unknown Error"}") }
        }) { Text("Compile") }
        HorizontalSpacer(10.dp)
        Button({
            executorState["programLoaded"] = false
            executorState["consoleText"] = ""
            crScope.launch {
                try {
                    STATE.processor.execute(STATE.programReadStart)
                } catch(e: Exception) { executorState["consoleText"] = "Error: ${e.message ?: "Unknown Error"}" }
            }
            setDebug(false)
        }) { Text("Run") }
        HorizontalSpacer(10.dp)
        Button({
            setDebug(true)
            crScope.launch {
                try {
                    STATE.processor.execute(STATE.programReadStart) { pc -> ((executorState["programCounter"] as UInt).toUShort() == pc) }
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
    TextField(executorState["content"]!! as String, { u -> executorState["content"] = u}, modifier = maxWidth.fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(executorState["consoleText"]!! as String, {}, readOnly = true, modifier = maxSize)
}