package parser

import java.util.function.DoubleBinaryOperator
import java.util.function.DoubleUnaryOperator
import kotlin.math.pow
import kotlin.math.sqrt

enum class Operators {

    PLUS(1, false, DoubleBinaryOperator { l: Double, r: Double -> l + r }),
    MINUS(1, false, DoubleBinaryOperator { l: Double, r: Double -> l - r }),
    MULTIPLY(2, false, DoubleBinaryOperator { l: Double, r: Double -> l * r }),
    DIVIDE(2, false, DoubleBinaryOperator { l: Double, r: Double -> l / r }),
    POW(3, true, DoubleBinaryOperator { a: Double, b: Double -> a.pow(b) }),
    NEGATION(3, DoubleUnaryOperator { operand: Double -> -operand }),
    SQRT(3, DoubleUnaryOperator { operand -> sqrt(operand) });


    val priority: Int
    val isRightAssociative: Boolean
    private val binaryOperator: DoubleBinaryOperator?
    private val unaryOperator: DoubleUnaryOperator?
    val isUnary: Boolean

    constructor(priority: Int, rightAssociative: Boolean, binaryOperator: DoubleBinaryOperator) {
        this.priority = priority
        isRightAssociative = rightAssociative
        this.binaryOperator = binaryOperator
        unaryOperator = null
        isUnary = false
    }

    constructor(priority: Int, unaryOperator: DoubleUnaryOperator) {
        this.priority = priority
        isRightAssociative = true
        binaryOperator = null
        this.unaryOperator = unaryOperator
        isUnary = true
    }

    fun compute(left: Double, right: Double): Double {
        if (isUnary) throw UnsupportedOperationException("This is a unary operator")
        return binaryOperator!!.applyAsDouble(left, right)
    }

    fun compute(operand: Double): Double {
        if (!isUnary) throw UnsupportedOperationException("This is a binary operator")
        return unaryOperator!!.applyAsDouble(operand)
    }
}
