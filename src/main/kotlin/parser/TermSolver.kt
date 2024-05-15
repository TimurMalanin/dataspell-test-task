package parser

import java.util.*

class UnknownOperatorException(message: String) : Exception(message)
class InvalidExpressionException(message: String) : Exception(message)
class InsufficientOperandsException(message: String) : Exception(message)


object TermSolver {
    private val operators: HashMap<String, Operator> = hashMapOf(
        "+" to BinaryOperator.PLUS,
        "-" to BinaryOperator.MINUS,
        "*" to BinaryOperator.MULTIPLY,
        "/" to BinaryOperator.DIVIDE,
        "^" to BinaryOperator.POW,
        "_" to UnaryOperator.NEGATION,
        "~" to UnaryOperator.SQRT,
    )

    private val functions = listOf("sqrt")

    fun transformInfixToPostfix(input: String): Optional<String> {
        val normalizedInfix = input.replace(" ", "")
        val opStack = Stack<String>()
        val postfix = StringJoiner(" ")
        val operand = StringBuilder()
        var expectUnary = true
        var index = 0

        while (index < normalizedInfix.length) {
            val currentChar = normalizedInfix[index].toString()

            when {
                isFunction(normalizedInfix, index) -> {
                    handleFunction(normalizedInfix, index, opStack, postfix, expectUnary)
                    index += functions[0].length
                    expectUnary = true
                }

                isNonOperator(currentChar, normalizedInfix[index]) -> {
                    handleNonOperator(normalizedInfix, index, operand, postfix)
                    index++
                }

                else -> {
                    flushOperand(operand, postfix)
                    processCharacter(currentChar, opStack, postfix, expectUnary)
                    expectUnary = currentChar == "("
                    index++
                }
            }
        }

        flushStackToPostfix(opStack, postfix)
        return Optional.of(postfix.toString())
    }

    fun solvePostfix(input: String): Double {
        val tokens = input.split(" ").filterNot { it.isEmpty() }
        val stack = Stack<Double>()

        tokens.forEach { token ->
            processToken(token, stack)
        }

        return when {
            stack.size == 1 -> stack.pop()
            else -> throw InvalidExpressionException("Invalid expression")
        }
    }

    private fun processOperatorForPostfix(
        operator: String,
        opStack: Stack<String>,
        postfix: StringJoiner,
        expectUnary: Boolean,
    ) {
        val operatorToAdd = when {
            expectUnary && operator == "-" -> "_"
            expectUnary && operator == "sqrt" -> "~"
            else -> operator
        }

        if (shouldPushOperatorToStack(opStack)) {
            opStack.push(operatorToAdd)
        } else {
            processOperatorsInStack(operatorToAdd, opStack, postfix)
        }
    }

    private fun handleFunction(
        input: String,
        index: Int,
        opStack: Stack<String>,
        postfix: StringJoiner,
        expectUnary: Boolean
    ) {
        val (function, _) = getFunction(input, index)
        when (function) {
            "sqrt" -> processOperatorForPostfix("~", opStack, postfix, expectUnary)
        }
    }

    private fun handleNonOperator(
        input: String,
        index: Int,
        operand: StringBuilder,
        postfix: StringJoiner
    ) {
        operand.append(input[index])
        if (isLastCharacter(index, input)) {
            postfix.add(operand.toString())
            operand.clear()
        }
    }

    private fun applyOperatorToStack(operator: String, stack: Stack<Double>) {
        val operation = operators[operator] ?: throw UnknownOperatorException("Unknown operator: $operator")
        when (operation) {
            is UnaryOperator -> applyUnaryOperator(operation, stack)
            is BinaryOperator -> applyBinaryOperator(operation, stack)
            else -> throw InvalidExpressionException("Unsupported operation type")
        }
    }

    private fun shouldPushOperatorToStack(opStack: Stack<String>) = opStack.isEmpty() || opStack.peek() == "("

    private fun processOperatorsInStack(
        currentOperator: String,
        opStack: Stack<String>,
        postfix: StringJoiner,
    ) {
        val currentOp =
            operators[currentOperator] ?: throw UnknownOperatorException("Operator not defined: $currentOperator")

        while (opStack.isNotEmpty() && opStack.peek() != "(") {
            val stackOp = operators[opStack.peek()] ?: throw UnknownOperatorException("Operator not defined on stack")
            if (currentOp.priority > stackOp.priority || (currentOp.priority == stackOp.priority && currentOp.isRightAssociative)) {
                break
            }
            postfix.add(opStack.pop())
        }
        opStack.push(currentOperator)
    }

    private fun getFunction(infix: String, index: Int): Pair<String, Int> {
        val matchingFunction = functions.firstOrNull { infix.startsWith(it, index) }
        return Pair(matchingFunction ?: "", matchingFunction?.length ?: 0)
    }

    private fun isFunction(infix: String, index: Int) = functions.any { infix.startsWith(it, index) }

    private fun isNonOperator(current: String, char: Char) =
        !operators.containsKey(current) && char != '(' && char != ')' && current != "~"

    private fun isLastCharacter(index: Int, normalizedInfix: String) = index == normalizedInfix.length - 1

    private fun flushOperand(operand: StringBuilder, postfix: StringJoiner) {
        if (operand.isNotEmpty()) {
            postfix.add(operand.toString())
            operand.clear()
        }
    }

    private fun processCharacter(current: String, opStack: Stack<String>, postfix: StringJoiner, expectUnary: Boolean) {
        when (current) {
            "(" -> opStack.push(current)
            ")" -> flushParentheses(opStack, postfix)
            in operators.keys -> processOperatorForPostfix(current, opStack, postfix, expectUnary)
            else -> throw UnknownOperatorException("Unknown operator: $current")
        }
    }

    private fun flushParentheses(opStack: Stack<String>, postfix: StringJoiner) {
        while (opStack.peek() != "(") {
            postfix.add(opStack.pop())
        }
        opStack.pop()
    }

    private fun flushStackToPostfix(opStack: Stack<String>, postfix: StringJoiner) {
        while (opStack.isNotEmpty()) {
            postfix.add(opStack.pop())
        }
    }

    private fun processToken(token: String, stack: Stack<Double>) {
        token.toDoubleOrNull()?.let {
            stack.push(it)
        } ?: applyOperatorToStack(token, stack)
    }

    private fun applyUnaryOperator(operator: Operator, stack: Stack<Double>) {
        if (stack.isEmpty()) throw InsufficientOperandsException("Invalid expression: Unary operator '${operator}' needs one operand.")
        stack.push(operator.compute(stack.pop()))
    }

    private fun applyBinaryOperator(operator: Operator, stack: Stack<Double>) {
        if (stack.size < 2) throw InsufficientOperandsException("Invalid expression: Binary operator '${operator}' needs two operands.")
        val right = stack.pop()
        val left = stack.pop()
        stack.push(operator.compute(left, right))
    }

}
