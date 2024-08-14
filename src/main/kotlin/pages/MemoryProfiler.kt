package pages

import androidx.compose.foundation.ExperimentalFoundationApi
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

@OptIn(ExperimentalStdlibApi::class, ExperimentalFoundationApi::class)
@Composable fun Profiler(sp: Int, memUpdate: Int) = Column(modifier = Modifier.fillMaxSize()) {
    Row(modifier = Modifier.fillMaxSize()) {
        val heapListState = rememberLazyListState()
        key(memUpdate) {
            LazyColumn(state = heapListState) {//all memory
                stickyHeader {
                    Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text("Address (hex)", modifier = Modifier.padding(horizontal = 2.dp))
                        Text("Value (bin)", modifier = Modifier.padding(horizontal = 2.dp))
                        Text("Value (hex)", modifier = Modifier.padding(horizontal = 2.dp))
                    }
                }
                itemsIndexed(SharedState.state.ram.statefulMemory, key = { index, _ -> memUpdate + index }) { index, byte ->
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
        }
        HorizontalSpacer(2.dp)
        VerticalScrollbar(modifier = Modifier.align(Alignment.Bottom).fillMaxHeight(), adapter = rememberScrollbarAdapter(scrollState = heapListState))
        HorizontalSpacer(6.dp)
        LazyColumn {//registers and stack ptr
            item {
                Text("Stack Pointer: $sp")
            }
        }
    }
}