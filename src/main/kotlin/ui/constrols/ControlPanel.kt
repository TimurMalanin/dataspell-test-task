package ui.constrols

import ui.UI
import javax.swing.JButton
import javax.swing.JPanel

class ControlPanel(private val listener: UI) {
    fun getView(): JPanel {
        return JPanel().apply {
            add(JButton("Add Row").apply { addActionListener { listener.addRow() } })
            add(JButton("Add Column").apply { addActionListener { listener.addColumn() } })
        }
    }
}
