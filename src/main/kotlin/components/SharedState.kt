package components

import Processor
import Ram
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class SharedState private constructor() {
    var memory: UInt = 32768u
    var stackSize: UInt = 1024u
    var stackStart: UInt = 1u
    var programMemory: UInt = 16384u
    var programMemoryStart: UInt = 16384u
    var ram = Ram(memory.toInt())
    lateinit var processor: Processor

    fun updateSettings() {
        ram = Ram(memory.toInt())
        processor = Processor(
            ram,
            stackRange = (stackStart.toInt()..(stackStart+stackSize).toInt()),
            programMemory = (programMemory.toInt()..(programMemoryStart+programMemory).toInt())

        )
    }

    fun initProcessor(processor: Processor) { this.processor = processor }

    companion object {
        val state = SharedState()
    }
}