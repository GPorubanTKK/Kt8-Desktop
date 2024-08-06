import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
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
import pages.Debugger
import pages.Executor
import pages.Profiler
import pages.Settings

fun main() {
    application {
        Window(
            title = "Kt8 Emulator",
            onCloseRequest = ::exitApplication
        ) {
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
                }
                Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    when (destination) {
                        Destination.Executor -> Executor()
                        Destination.Screen -> Debugger()
                        Destination.Profiler -> Profiler()
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