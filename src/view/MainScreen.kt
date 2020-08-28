package view

import model.GameBoard
import model.GameBoardEvent
import javax.swing.JFrame
import javax.swing.JOptionPane

fun main(args: Array<String>) {
    MainScreen()
}

class MainScreen : JFrame() {

    private val gameBoard = GameBoard(qtdLine = 16, qtdColumn = 30, qtdMine = 89)
    private val gameBoardPanel = GameBoardPanel(gameBoard)

    init {
        gameBoard.onEvent(this::showResult)
        add(gameBoardPanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun showResult(event: GameBoardEvent) {
        javax.swing.SwingUtilities.invokeLater {
            val msg = when (event) {
                GameBoardEvent.WINNING -> "Você Ganhou!"
                GameBoardEvent.DEFEAT -> "Você perdeu..."
            }

            JOptionPane.showMessageDialog(this, msg)
            gameBoard.restart()

            gameBoardPanel.repaint()
            gameBoardPanel.validate()
        }
    }
}