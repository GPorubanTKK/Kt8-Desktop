import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.HorizontalSpacer
import components.NavIcon
import components.SharedState
import components.rememberMutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pages.Executor
import pages.Graphics
import pages.Profiler
import pages.Settings
import kotlin.math.min

fun main() {
    application {
        Window(
            title = "Kt8 Emulator",
            onCloseRequest = ::exitApplication
        ) {
            val (getDebug, setDebug) = rememberMutableStateOf(false)
            val (getSp, setSp) = rememberMutableStateOf(0)
            val (getMu, setMu) = rememberMutableStateOf(0)
            val globalCrScope = rememberCoroutineScope { Dispatchers.IO }
            val executorStateMap = remember { mutableStateMapOf<String, Any>(
                "path" to System.getProperty("user.home") + "\\Desktop\\or.gasm",
                "content" to "",
                "consoleText" to "",
                "programLoaded" to false,
                "programLength" to 0u,
                "programCounter" to SharedState.state.programReadStart + 3u
            ) }
            Column(modifier = Modifier.fillMaxSize()) {
                val (destination, setDestination) = rememberMutableStateOf(Destination.Executor)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .padding(bottom = 4.dp)
                        .shadow(1.dp)
                ) {
                    HorizontalSpacer(10.dp)
                    Destination.entries.forEach {
                        NavIcon(
                            it.title,
                            it.icon,
                            it,
                            destination,
                            setDestination
                        )
                        HorizontalSpacer(2.dp)
                    }
                    HorizontalSpacer(25.dp)
                    if(destination != Destination.Executor) {
                        Button({
                            executorStateMap["programLoaded"] = false
                            executorStateMap["consoleText"] = ""
                            executorStateMap["programCounter"] = SharedState.state.programReadStart + 3u
                            globalCrScope.launch {
                                SharedState.state.processor.execute(SharedState.state.programReadStart)
                            }
                            setDebug(false)
                        }) { Text("Run") }
                        HorizontalSpacer(10.dp)
                        Button({
                            setDebug(true)
                            executorStateMap["programCounter"] = SharedState.state.programReadStart + 3u
                            globalCrScope.launch {
                                SharedState.state.processor.execute(SharedState.state.programReadStart) { pc ->
                                    (executorStateMap["programCounter"] as UInt == pc)
                                }
                                setDebug(false)
                            }
                        }) { Text("Debug") }
                        HorizontalSpacer(10.dp)
                        Button({
                            executorStateMap["programCounter"] = min(executorStateMap["programLength"] as UInt + executorStateMap["programCounter"] as UInt, executorStateMap["programCounter"] as UInt + 3u)
                        }, enabled = getDebug) { Icon(Icons.Filled.FastForward, "") }
                    }
                }
                Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    when (destination) {
                        Destination.Executor -> Executor(
                            getDebug,
                            setDebug,
                            executorStateMap,
                            setSp,
                            setMu
                        )
                        Destination.Screen -> Graphics()
                        Destination.Profiler -> Profiler(getSp, getMu)
                        Destination.Settings -> Settings()
                    }
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