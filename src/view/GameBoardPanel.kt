package view

import model.GameBoard
import java.awt.GridLayout
import javax.swing.Panel

class GameBoardPanel(gameBoard: GameBoard) : JPanel() {

    init {
        layout = GridLayout(gameBoard.qtdLine, gameBoard.qtdColumn)
        gameBoard.forEachField { field ->
            val button = FieldButton(field)
            add(button)
        }
    }
}