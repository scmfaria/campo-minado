package model

enum class GameBoardEvent { WINNING, DEFEAT }

class GameBoard(val qtdLine: Int, val qtdColumn: Int, val qtdMine: Int) {
    private val fields = ArrayList<ArrayList<Field>>
    private val callbacks = ArrayList<(GameBoard) -> Unit>()

    init {
        generateFields()
        associateNeighbors()
        drawMines()
    }

    private fun generateFields() {
        for(line in 0 until qtdLine) {
            fields.add(ArrayList())
            for (column in 0 until qtdColumn) {
                val newField = Field(line, column)
                fields[line].add(newField)
            }
        }
    }

    private fun associateNeighbors() {
        forEachField { associateNeighbors(it) }
    }

    private fun associateNeighbors(field: Field) {
        val (line, column) = field
        val lines = arrayOf(line -1, line, line + 1)
        val columns = arrayOf(column -1, column, column + 1)

        lines.forEach { l ->
            columns.forEach { c ->
                val current = fields.getOrNull(l)?.getOrNull(c)
                current?.takeIf { field != it }?.let { field.addNeighbor(it)}
            }
        }
    }

    private fun drawMines() {
        val generate = Random()

        var drawnLine = -1
        var drawnColumn = -1
        var qtdCurrentMine = 0

        while (qtdCurrentMine < this.qtdMine) {
            drawnLine = generate.nextInt(qtdLine)
            drawnColumn = generate.nextInt(qtdColumn)

            val drawnField = fields[drawnLine][drawnColumn]
            if(drawnField.safe) {
                drawnField.mine()
                qtdMine++
            }
        }
    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { line -> line.forEach(callback) }
    }
}