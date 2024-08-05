package pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.VerticalSpacer
import components.rememberMutableStateOf
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.FileReader
import java.io.File
@Composable fun Executor(buf: ByteArrayOutputStream) = Column(modifier = Modifier.fillMaxSize()) {
    VerticalSpacer(5.dp)
    Text("Run Source File:")
    var path by rememberMutableStateOf(System.getProperty("user.home") + "\\Desktop\\")
    var content by rememberMutableStateOf("")
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(path, {path = it})
        HorizontalSpacer(10.dp)
        Button({
            content = try {
                println("test")
                BufferedReader(FileReader(File(path))).readText()
            } catch(e: Exception) { "${e.message}: ${e.cause}" }
        }) { Text("Load") }
    }
    VerticalSpacer(12.dp)
    TextField(content, {content = it}, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
    VerticalSpacer(5.dp)
    TextField(buf.toString(), {}, readOnly = true, modifier = Modifier.fillMaxSize())
}