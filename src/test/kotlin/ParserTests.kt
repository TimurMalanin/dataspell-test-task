import org.junit.jupiter.api.Test
import parser.TermSolver
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TermSolverTest {

    @Test
    fun testSimpleAddition() {
        val result = TermSolver.transformInfixToPostfix("2 + 3")
        assertTrue(result.isPresent, "Transform infix to postfix failed")
        assertEquals("2 3 +", result.get(), "Simple addition failed")
    }

    @Test
    fun testOperatorPrecedence() {
        val result = TermSolver.transformInfixToPostfix("2 + 3 * 4")
        assertTrue(result.isPresent, "Transform infix to postfix failed")
        assertEquals("2 3 4 * +", result.get(), "Operator precedence failed")
    }

    @Test
    fun testParentheses() {
        val result = TermSolver.transformInfixToPostfix("(2 + 3) * 4")
        assertTrue(result.isPresent, "Transform infix to postfix failed")
        assertEquals("2 3 + 4 *", result.get(), "Parentheses failed")
    }

    @Test
    fun testUnaryNegation() {
        val result = TermSolver.transformInfixToPostfix("-3 + 4")
        assertTrue(result.isPresent, "Transform infix to postfix failed")
        assertEquals("3 _ 4 +", result.get(), "Unary negation failed")
    }

    @Test
    fun testExponentiationRightAssociativity() {
        val result = TermSolver.transformInfixToPostfix("2 ^ 3 ^ 4")
        assertTrue(result.isPresent, "Transform infix to postfix failed")
        assertEquals("2 3 4 ^ ^", result.get(), "Exponentiation right associativity failed")
    }

    @Test
    fun testSolvePostfixSimpleAddition() {
        val result = TermSolver.solvePostfix("2 3 +")
        assertEquals(5.0, result, 0.01, "Solve postfix simple addition failed")
    }

    @Test
    fun testSolvePostfixWithUnaryNegation() {
        val result = TermSolver.solvePostfix("3 _ 4 +")
        assertEquals(1.0, result, 0.01, "Solve postfix with unary negation failed")
    }

    @Test
    fun testSolvePostfixWithComplexExpression() {
        val result = TermSolver.solvePostfix("2 3 + 4 * 5 -")
        assertEquals(15.0, result, 0.01, "Solve postfix with complex expression failed")
    }

    @Test
    fun testPowFunction() {
        val result = TermSolver.transformInfixToPostfix("pow(2, 3)")
        assertTrue(result.isPresent, "Named function call failed")
        assertEquals("pow 2,3", result.get(), "Pow function failed")
    }

    @Test
    fun testInvalidPostfixExpression() {
        assertFailsWith<Exception>("Invalid postfix expression exception not thrown") {
            TermSolver.solvePostfix("2 3 * +")
        }
    }

    @Test
    fun testUnknownOperatorInPostfix() {
        assertFailsWith<Exception>("Unknown operator in postfix exception not thrown") {
            TermSolver.solvePostfix("2 3 @")
        }
    }
}
