package ui.formula

import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class TableModelListener(private val model: DefaultTableModel) : TableModelListener {
    private val formulaProcessor: FormulaProcessor = FormulaProcessor(model, this)

    override fun tableChanged(e: TableModelEvent) {
        if (e.type == TableModelEvent.UPDATE && e.firstRow >= 0 && e.column >= 0) {
            val cellValue = model.getValueAt(e.firstRow, e.column) as? String ?: return
            if (cellValue.startsWith("=")) {
                formulaProcessor.processFormula(cellValue, e.firstRow, e.column)
            }
        }
    }
}
