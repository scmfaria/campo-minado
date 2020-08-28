package model

enum class EventField { OPENING, MARK, UNMARK, EXPLOSION, REINITIALIZATION }

data class Field(val line: Int, val column: Int) {

    private val neighborsList = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, EventField) -> Unit>()

    var marked: Boolean = false
    var open: Boolean = false
    var mine: Boolean = false

    val unmarked: Boolean get() = !marked
    val close: Boolean get() = !open
    val safe: Boolean get() = !mine

    val goalArchieved: Boolean get() = safe && open || mine && marked

    val qtdMarkedNeighbors: Int get() = neighborsList.filter { it.mine }.size

    private val safeNeighbors: Boolean
        get() = neighborsList.map { it.safe }.reduce { result, safe -> result && safe }

    fun addNeighbor(neighbor: Field) {
        neighborsList.add(neighbor)
    }

    fun onEvent(callback: (Field, EventField) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (close) {
            open = true
            if (mine) {
                callbacks.forEach { it(this, EventField.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, EventField.OPENING) }
                // it.open() - recursividade, pois esta chamando o metodo abrir dos seus vizinhos
                neighborsList.filter { it.close && it.safe && safeNeighbors }.forEach { it.open() }
            }
        }
    }

    fun changeMarkup() {
        if (close) {
            marked = !marked
            val event = if(marked) EventField.MARK else EventField.UNMARK
            callbacks.forEach { it(this, event) }
        }
    }

    fun mine() {
       mine = true
    }

    fun reboot() {
        open = false
        mine = false
        marked = false
        callbacks.forEach { it(this, EventField.REINITIALIZATION) }
    }
}