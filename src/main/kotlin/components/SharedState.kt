package components

import Processor
import Ram

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
            stackRange = (stackStart..(stackStart+stackSize)),
            programMemory = (programMemory..(programMemoryStart+programMemory))

        )
    }

    companion object {
        val state = SharedState()
    }
}