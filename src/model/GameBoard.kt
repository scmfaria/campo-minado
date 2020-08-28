package model

import java.util.*
import kotlin.collections.ArrayList

enum class GameBoardEvent { WINNING, DEFEAT }

class GameBoard(val qtdLine: Int, val qtdColumn: Int, var qtdMine: Int) {
    private val fields = ArrayList<ArrayList<Field>>()
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
                newField.onEvent(this::verifyDefeatOrVictory)
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
        val qtdCurrentMine = 0

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

    private fun goalAchieved(): Boolean {
        var playerWon = true
        forEachField { if(!it.goalArchieved) playerWon = false }
        return playerWon
    }

    private fun verifyDefeatOrVictory(field: Field, event: EventField) {
        if (event == EventField.EXPLOSION) {
            callbacks.forEach { it(GameBoardEvent.DEFEAT) }
        } else if (goalAchieved()) {
            callbacks.forEach { it(GameBoardEvent.WINNING) }
        }
    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { line -> line.forEach(callback) }
    }

    fun onEvent(callback: (GameBoardEvent) -> Unit) {
        callbacks.add { callback }
    }

    fun restart() {
        forEachField { it.reboot() }
        drawMines()
    }
}