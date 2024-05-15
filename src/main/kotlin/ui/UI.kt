package ui

import ui.constrols.ControlPanel
import java.awt.BorderLayout
import javax.swing.*

class UI {
    private val frame = JFrame("Table")
    private val tableView: TableView = TableView()
    private val controlPanel: ControlPanel = ControlPanel(this)

    init {
        configureUI()
    }

    private fun configureUI() {
        frame.apply {
            setSize(500, 500)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            add(tableView.getView(), BorderLayout.CENTER)
            add(controlPanel.getView(), BorderLayout.SOUTH)
            isVisible = true
        }
    }

    fun addRow() {
        tableView.addRow()
    }

    fun addColumn() {
        tableView.addColumn()
    }
}
