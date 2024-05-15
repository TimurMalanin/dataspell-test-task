package parser

import kotlin.math.pow
import kotlin.math.sqrt

interface Operator {
    val priority: Int
    val isRightAssociative: Boolean
    fun compute(vararg operands: Double): Double
}

enum class UnaryOperator(
    override val priority: Int, override val isRightAssociative: Boolean, private val unaryOperator: (Double) -> Double
) : Operator {

    NEGATION(3, true, { operand -> -operand }),
    SQRT(3, true, { operand -> sqrt(operand) });

    override fun compute(vararg operands: Double): Double {
        if (operands.size != 1) throw IllegalArgumentException("Unary operator needs exactly one operand")
        return unaryOperator(operands.first())
    }
}

enum class BinaryOperator(
    override val priority: Int,
    override val isRightAssociative: Boolean,
    private val binaryOperator: (Double, Double) -> Double
) : Operator {

    PLUS(1, false, { l, r -> l + r }),
    MINUS(1, false, { l, r -> l - r }),
    MULTIPLY(2, false, { l, r -> l * r }),
    DIVIDE(2, false, { l, r -> l / r }),
    POW(3, true, { a, b -> a.pow(b) });

    override fun compute(vararg operands: Double): Double {
        if (operands.size != 2) throw IllegalArgumentException("Binary operator needs exactly two operands")
        return binaryOperator(operands[0], operands[1])
    }
}