package pages

import Compiler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.*
import components.SharedState.Companion.state
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader

@Composable fun Executor(buf: ByteArrayOutputStream) = Column(modifier = Modifier.fillMaxSize()) {
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    var path by rememberMutableStateOf(System.getProperty("user.home") + "\\Desktop\\or.gasm")
    var content by rememberMutableStateOf("")
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
    /*key(consoleBuffer.value.size()) {
        TextField(consoleBuffer.value.toString(), {}, readOnly = false, modifier = Modifier.fillMaxSize())
    }*/
}