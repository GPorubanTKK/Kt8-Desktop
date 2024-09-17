package pages

import Compiler
import Processor
import WriteTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import components.*
import components.SharedState.Companion.STATE
import components.maxSize
import components.maxWidth
import kotlinx.coroutines.*
import java.io.*
import kotlin.math.min

@Composable fun Executor(
    pathToLoad: MutableState<String>,
    editorContent: MutableState<String>,
    consoleContent: MutableState<String>,
    isProgramLoaded: MutableState<Boolean>,
    programLength: MutableState<UShort>,
    debugProgramCounter: MutableState<UInt>,
    memoryUpdate: MutableState<Int>,
    debugState: MutableState<Boolean>,
    crScope: CoroutineScope
) = Column(modifier = maxSize) {
    val (path, setPath) = pathToLoad
    val (console, setConsole) = consoleContent
    val (editor, setEditor) = editorContent
    val (debug, setDebug) = debugState
    val (counter, setCounter) = debugProgramCounter
    val (length, setLength) = programLength
    val (_, updateMemory) = memoryUpdate
    val (_, setIsLoaded) = isProgramLoaded
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    SideEffect {
        try { STATE.processor.toString() } catch(e: Exception) {
            STATE.processor = Processor(
                STATE.ram,
                stackRange = (STATE.stackStart..(STATE.stackStart + STATE.stackSize).toUShort()),
                programMemory = (STATE.programMemory..(STATE.programMemoryStart+STATE.programMemory).toUShort()),
                outputStream = object : WriteTarget { override fun print(str: String) { setConsole(console + str) } },
                onMemoryUpdate = { pc ->
                    updateMemory(pc)
                    println("Updating fn: $pc")
                }
            )
        }
    }
    Row(modifier = maxWidth) {
        TextField(path, setPath)
        HorizontalSpacer(10.dp)
        Button({
            setEditor(try { BufferedReader(FileReader(path)).readText() } catch(e: Exception) { "${e.message}: ${e.cause}" })
        }) { Text("Load") }
        HorizontalSpacer(10.dp)
        Button({
            try {
                val compiledGasm = Compiler().compileGasm(editor)
                println("Compiled gASM ${compiledGasm.toList()}")
                STATE.processor.reset()
                STATE.ram.load(compiledGasm, STATE.programReadStart.toInt())
                setIsLoaded(true)
                setLength((compiledGasm.size - 3).toUShort())
                setCounter(STATE.programReadStart + 3u)
            } catch(e: Exception) { println("Error: ${e.message ?: "Unknown Error"}") }
        }) { Text("Compile") }
        HorizontalSpacer(10.dp)
        Button({
            setIsLoaded(false)
            setConsole("")
            crScope.launch {
                try {
                    STATE.processor.execute(STATE.programReadStart)
                } catch(e: Exception) { setConsole("$console \nError: ${e.message ?: "Unknown Error"}") }
            }
            setDebug(false)
        }) { Text("Run") }
        HorizontalSpacer(10.dp)
        Button({
            setDebug(true)
            crScope.launch {
                try {
                    STATE.processor.execute(STATE.programReadStart) { pc -> (counter.toUShort() == pc) }
                } catch(e: Exception) { setConsole("$console \nError: ${e.message ?: "Unknown Error"}") }
                setDebug(false)
            }
        }) { Text("Debug") }
        HorizontalSpacer(10.dp)
        Button({ setCounter(min(length + counter, counter + 3u)) }, enabled = debug) {
            Icon(Icons.Filled.FastForward, "")
        }
    }
    VerticalSpacer(12.dp)
    TextField(editor, setEditor, modifier = maxWidth.fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(console, {}, readOnly = true, modifier = maxSize)
}