package view

import model.Field
import java.awt.Font
import javax.swing.JButton
import javax.swing.BorderFactory
import java.awt.Color

private val COLOR_BG_NORMAL = Color(184, 184, 184)
private val COLOR_BG_MARK = Color(8, 179, 247)
private val COLOR_BG_EXPLOSION = Color(189, 66, 68)
private val COLOR_TXT_GREEN = Color(0, 100, 0)

class FieldButton(private val field: Field): JButton() {

    init {
        font = font.deriveFont(Font.BOLD)
        background = COLOR_BG_NORMAL
        isOpague = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, { it.open() }, { it.changeMarkup() }))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, event: EventField) {
        when(event) {
            EventField.EXPLOSION -> applyExplosionStyle()
            EventField.OPENING -> applyOpeningStyle()
            EventField.MARK -> applyMarkStyle()
            else -> applyStandardStyle()
        }

        javax.swing.SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyExplosionStyle() {
        background = COLOR_BG_EXPLOSION
        text = "X"
    }

    private fun applyOpeningStyle() {
        background = COLOR_BG_NORMAL
        border = BorderFactory.createLineBorder(java.awt.Color.GRAY)

        foreground = when (field.qtdMarkedNeighbors) {
            1 -> COLOR_TXT_GREEN
            2 -> BLUE
            3 -> YELLOW
            4, 5, 6 -> RED
            else -> PINK
        }

        text = if (field.qtdMarkedNeighbors > 0) field.qtdMarkedNeighbors.toString() else ""
    }

    private fun applyMarkStyle() {
        background = COLOR_BG_MARK
        foreground = BLACK
        text = "M"
    }

    private fun applyStandardStyle() {
        background = COLOR_BG_NORMAL
        foreground = BorderFactory.createBevelBorder(0)
        text = ""
    }
}