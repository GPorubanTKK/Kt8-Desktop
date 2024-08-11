package pages

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState

@OptIn(ExperimentalStdlibApi::class)
@Composable fun Profiler() = Column(modifier = Modifier.fillMaxSize()) {
    val memAsState = remember { SharedState.state.ram.statefulMemory }
    Row(modifier = Modifier.fillMaxSize()) {
        val heapListState = rememberLazyListState()
        LazyColumn(state = heapListState) {//all memory
            item {
                Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text("Memory Address (hex)")
                    Text("Value (bin)")
                    Text("Value (hex)")
                }
            }
            itemsIndexed(memAsState) { index, byte ->
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f).border(0.05.dp, Color(0xFFE6E6E6)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(index.toHexString(HexFormat.Default))
                    Text(byte.toString(2))
                    Text(byte.toHexString(HexFormat.Default))
                }
            }
        }
        HorizontalSpacer(2.dp)
        VerticalScrollbar(modifier = Modifier.align(Alignment.Bottom).fillMaxHeight(), adapter = rememberScrollbarAdapter(scrollState = heapListState))
        HorizontalSpacer(6.dp)
        LazyColumn {//registers and stack ptr
            item { Text("Stack Pointer: NOT IMPLEMENTED") }
        }
    }
}