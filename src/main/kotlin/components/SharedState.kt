package components

class SharedState private constructor() {
    var memory: UInt = 32768u
    var stackSize: UInt = 1024u
    var stackStart: UInt = 1u
    var programMemory: UInt = 16384u
    var programMemoryStart: UInt = 16384u

    companion object { val state = SharedState() }
}