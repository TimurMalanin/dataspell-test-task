package ui.formula

import parser.TermSolver.solvePostfix
import parser.TermSolver.transformInfixToPostfix
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class InvalidCellReferenceException(message: String) : Exception(message)

class FormulaProcessor(private val model: DefaultTableModel, private val tableModelListener: TableModelListener) {
    fun processFormula(formula: String, row: Int, col: Int) {
        try {
            val result = splitFormula(formula).joinToString("", transform = ::processPart)

            transformInfixToPostfix(result).takeIf { it.isPresent }?.let {
                val resultNum = solvePostfix(it.get())

                if (resultNum.isInfinite() || resultNum.isNaN()) {
                    throw ArithmeticException("Division by zero")
                }

                val valueToSet = if (resultNum % 1.0 == 0.0) resultNum.toInt() else resultNum
                updateModel(valueToSet , row, col)
            }
        } catch (e: InvalidCellReferenceException) {
            model.setValueAt("Error: Invalid cell reference", row, col)
        } catch (e: ArithmeticException) {
            model.setValueAt("Error: Division by zero", row, col)
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
            val row = part.substring(1).toInt() - 1
            val col = part[0] - 'A' + 1
            try {
                val value = model.getValueAt(row, col)?.toString()
                value ?: throw InvalidCellReferenceException("Invalid cell reference")
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw InvalidCellReferenceException("Invalid cell reference")
            }
        }
        else -> part
    }

    private fun updateModel(resultNum: Number, row: Int, col: Int) {
        model.removeTableModelListener(tableModelListener)
        model.setValueAt(resultNum, row, col)
        model.addTableModelListener(tableModelListener)
    }
}
