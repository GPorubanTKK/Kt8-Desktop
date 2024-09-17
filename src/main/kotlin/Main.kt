import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.*
import components.SharedState.Companion.STATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pages.Executor
import pages.Graphics
import pages.Profiler
import pages.Settings
import kotlin.math.min

fun main() = application {
    Window(title = "Kt8 Emulator", onCloseRequest = ::exitApplication) {
        val debugState = rememberMutableStateOf(false); val (debug, setDebug) = debugState
        val memoryUpdate = rememberMutableStateOf(0)
        val loadPath = rememberMutableStateOf("${System.getProperty("user.home")}\\Desktop\\src.gasm")
        val editorContent = rememberMutableStateOf("")
        val consoleContent = rememberMutableStateOf(""); val (_, setConsole) = consoleContent
        val isProgramLoaded = rememberMutableStateOf(false)
        val lengthOfProgram = rememberMutableStateOf<UShort>(0u); val (length, _) = lengthOfProgram
        val debugProgramCounter = rememberMutableStateOf(STATE.programReadStart + 3u); val (counter, setCounter) = debugProgramCounter
        val globalCrScope = rememberCoroutineScope { Dispatchers.IO }
        Column(modifier = maxSize) {
            val (destination, setDestination) = rememberMutableStateOf(Destination.Executor)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = maxWidth.fillMaxHeight(0.1f).padding(bottom = 4.dp).shadow(1.dp)) {
                HorizontalSpacer(10.dp)
                Destination.entries.forEach {
                    NavIcon(it.title, it.icon, it, destination, setDestination)
                    HorizontalSpacer(2.dp)
                }
                HorizontalSpacer(25.dp)
                if(destination != Destination.Executor) {
                    Button({
                        setConsole("")
                        setCounter(STATE.programReadStart + 3u)
                        globalCrScope.launch {
                            STATE.processor.execute(STATE.programReadStart)
                        }
                        setDebug(false)
                    }) { Text("Run") }
                    HorizontalSpacer(10.dp)
                    Button({
                        setDebug(true)
                        setCounter(STATE.programReadStart.toUInt())
                        globalCrScope.launch {
                            STATE.processor.execute(STATE.programReadStart) { pc -> counter.toUShort() == pc }
                            setDebug(false)
                        }
                    }) { Text("Debug") }
                    HorizontalSpacer(10.dp)
                    Button({ setCounter(min(length + counter, counter + 3u)) }, enabled = debug) {
                        Icon(Icons.Filled.FastForward, "")
                    }
                }
            }
            Column(modifier = maxSize.padding(10.dp)) {
                when (destination) {
                    Destination.Executor -> Executor(
                        loadPath,
                        editorContent,
                        consoleContent,
                        isProgramLoaded,
                        lengthOfProgram,
                        debugProgramCounter,
                        memoryUpdate,
                        debugState,
                        globalCrScope
                    )
                    Destination.Screen -> Graphics()
                    Destination.Profiler -> Profiler(memoryUpdate)
                    Destination.Settings -> Settings()
                }
            }
        }
    }
}

enum class Destination(val title: String, val icon: ImageVector) {
    Executor("Run", Icons.Filled.DeveloperMode),
    Screen("Graphics", Icons.Filled.Tv),
    Profiler("Memory Profiler", Icons.Filled.CalendarToday),
    Settings("Settings", Icons.Filled.Settings)
}