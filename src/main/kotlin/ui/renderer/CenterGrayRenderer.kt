package ui.renderer

import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

class CenterGrayRenderer(table: JTable) : DefaultTableCellRenderer() {
    init {
        background = table.tableHeader.background
        horizontalAlignment = CENTER
    }

    override fun getTableCellRendererComponent(
        table: JTable?, value: Any?, isSelected: Boolean,
        hasFocus: Boolean, row: Int, column: Int
    ): Component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
}
