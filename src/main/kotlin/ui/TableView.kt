package ui

import ui.formula.TableModelListener
import ui.renderer.CenterGrayRenderer
import javax.swing.DefaultListSelectionModel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class TableView {
    private val tableModel = createTableModel()
    private val table = JTable(tableModel).apply {
        autoResizeMode = JTable.AUTO_RESIZE_OFF
    }

    init {
        configureTableView()
    }

    fun getView(): JScrollPane = JScrollPane(table).apply {
        verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    }

    fun addRow() {
        val newRow = Array(tableModel.columnCount) { "" }
        tableModel.addRow(newRow)
        updateRowNumbers()
    }

    fun addColumn() {
        tableModel.addColumn(getColumnLabel(tableModel.columnCount - 1))
        restoreColumnSettings()
    }

    private fun configureTableView() {
        setupTableSelectionModel()
        table.model.addTableModelListener(TableModelListener(tableModel))
        table.getColumn("#").cellRenderer = CenterGrayRenderer(table)
    }

    private fun setupTableSelectionModel() {
        val columnModel = table.columnModel
        columnModel.selectionModel = object : DefaultListSelectionModel() {
            override fun setSelectionInterval(index0: Int, index1: Int) {
                super.setSelectionInterval(maxOf(index0, 1), maxOf(index1, 1))
            }
        }
        table.setColumnSelectionAllowed(true)
    }

    private fun createTableModel(): DefaultTableModel {
        val data = arrayOf(
            arrayOf("", "1", "2", "=A1+B1"),
            arrayOf("", "4", "5", "=A2+B2")
        )

        val columnNames = arrayOf("#", "A", "B", "C")
        return object : DefaultTableModel(data, columnNames) {
            override fun isCellEditable(row: Int, column: Int) = column != 0

            init {
                data.indices.forEach { rowIndex ->
                    setValueAt(rowIndex + 1, rowIndex, 0)
                }
            }
        }
    }

    private fun updateRowNumbers() {
        for (i in 0..<tableModel.rowCount) {
            tableModel.setValueAt((i + 1).toString(), i, 0)
        }
    }

    private fun getColumnLabel(index: Int) = generateSequence(index + 1) { (it - 1) / 26 }
        .map { 'A' + (it - 1) % 26 }
        .takeWhile { it != 'A' - 1 }
        .toList()
        .reversed()
        .joinToString("")


    private fun restoreColumnSettings() {
        table.getColumn("#").cellRenderer = CenterGrayRenderer(table)
        updateRowNumbers()
    }
}
