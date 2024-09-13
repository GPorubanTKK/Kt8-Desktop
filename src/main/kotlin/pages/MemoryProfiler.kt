package pages

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.HorizontalSpacer
import components.SharedState

@OptIn(ExperimentalStdlibApi::class)
@Composable fun Profiler(memUpdate: Int) = Column(modifier = Modifier.fillMaxSize()) {
    Row(modifier = Modifier.fillMaxSize()) {
        val heapListState = rememberLazyListState()
        key(memUpdate) {
            LazyColumn(state = heapListState) {//all memory
                item {
                    Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text("Address (hex)", modifier = Modifier.padding(horizontal = 2.dp))
                        Text("Value (hex)", modifier = Modifier.padding(horizontal = 2.dp))
                    }
                }
                itemsIndexed(SharedState.state.ram.toList().chunked(16), key = { index, _ -> memUpdate + index }) { index, bytes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f).border(0.05.dp, Color(0xFFE6E6E6)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text((index * 16).toHexString(HexFormat.UpperCase), modifier = Modifier.padding(start = 5.dp))
                        for(byte in bytes) Text(byte.toByte().toHexString(HexFormat.UpperCase) + "\t")
                    }
                }
            }
        }
        HorizontalSpacer(2.dp)
        VerticalScrollbar(modifier = Modifier.align(Alignment.Bottom).fillMaxHeight(), adapter = rememberScrollbarAdapter(scrollState = heapListState))
        HorizontalSpacer(6.dp)
        Column {//registers and stack ptr
            key(memUpdate) {
                val values = SharedState.state.processor.getRegisterValues()
                val codes = listOf("Accumulator", "W", "X", "Y", "Z")
                for(index in codes.indices) Text("${codes[index]}: ${values[index]}")
            }
        }
    }
}