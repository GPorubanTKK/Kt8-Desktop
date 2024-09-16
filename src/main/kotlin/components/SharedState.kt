package components

import Processor
import Ram

class SharedState private constructor() {
    var memory: UShort = 32768u
    var stackSize: UByte = 255u
    var stackStart: UShort = 0u
    var programMemory: UShort = 16384u
    var programMemoryStart: UShort = 16384u
    var ram = Ram(memory.toInt())
    var programReadStart: UShort = 16384u
    lateinit var processor: Processor

    fun updateSettings() {
        ram = Ram(memory.toInt())
    }

    companion object {
        val STATE = SharedState()
    }
}