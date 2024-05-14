package ui.formula

import parser.TermSolver.solvePostfix
import parser.TermSolver.transformInfixToPostfix
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class FormulaProcessor(private val model: DefaultTableModel, private val tableModelListener: TableModelListener) {
    fun processFormula(formula: String, row: Int, col: Int) {
        try {
            val result = splitFormula(formula).joinToString("", transform = ::processPart)

            transformInfixToPostfix(result).takeIf { it.isPresent }?.let {
                updateModel(solvePostfix(it.get()), row, col)
            }
        } catch (e: Exception) {
            model.setValueAt("Error: ${e.message}", row, col)
        }
    }

    private fun splitFormula(formula: String): List<String> =
        formula.substring(1).split(Regex("\\s+|(?<=[+\\-*/^()])|(?=[+\\-*/^()])|(?<=sqrt\\()|(?=\\))"))
            .filter { it.isNotEmpty() }
            .map { it.trim() }

    private fun processPart(part: String): String = when {
        part.length > 1 && part[0].isLetter() && part[1].isDigit() -> {
            val (col, row) = part[0] to part.substring(1).toInt()
            model.getValueAt(row - 1, col - 'A' + 1).toString()
        }
        else -> part
    }

    private fun updateModel(resultNum: Double, row: Int, col: Int) {
        model.removeTableModelListener(tableModelListener)
        model.setValueAt(resultNum, row, col)
        model.addTableModelListener(tableModelListener)
    }
}
