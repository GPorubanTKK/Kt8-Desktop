package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable fun HorizontalSpacer(width: Dp, modifier: Modifier = Modifier) = Spacer(modifier.width(width).height(0.dp))

@Composable fun VerticalSpacer(height: Dp, modifier: Modifier = Modifier) = Spacer(modifier.width(0.dp).height(height))

@Composable fun <T> rememberMutableStateOf(value: T) = remember { mutableStateOf(value) }

infix fun String.matches(pattern: String): Boolean = matches(pattern.toRegex())

@Composable fun NumTextField(
    currentText: String, upperBound: Int = Int.MAX_VALUE, lowerBound: Int = Int.MIN_VALUE,
    modifier: Modifier = Modifier, onUpdate: (Int) -> Unit) = TextField(currentText, {
    if(it matches "^\\d+$" && it.toInt() <= upperBound && it.toInt() >= lowerBound) onUpdate(it.toInt())
}, modifier = modifier)

val maxWidth = Modifier.fillMaxWidth()
val maxHeight = Modifier.fillMaxHeight()
val maxSize = Modifier.fillMaxSize()
