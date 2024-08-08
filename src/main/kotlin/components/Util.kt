package components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable fun HorizontalSpacer(width: Dp, modifier: Modifier = Modifier) = Spacer(modifier.width(width).height(0.dp))
@Composable fun VerticalSpacer(height: Dp, modifier: Modifier = Modifier) = Spacer(modifier.width(0.dp).height(height))
@Composable fun <T> rememberMutableStateOf(value: T) = remember { mutableStateOf(value) }
