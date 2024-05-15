# Table Editor

## Supported Operations

The application recognizes the following operations:
- **Unary Operators:**
    - `NEGATION`: Negates the value (e.g., `-x`).
    - `SQRT`: Computes the square root (e.g., `sqrt(x)`).
- **Binary Operators:**
    - `PLUS`: Adds two operands (e.g., `x + y`).
    - `MINUS`: Subtracts the second operand from the first (e.g., `x - y`).
    - `MULTIPLY`: Multiplies two operands (e.g., `x * y`).
    - `DIVIDE`: Divides the first operand by the second (e.g., `x / y`).
    - `POW`: Raises the first operand to the power of the second (e.g., `x ^ y`).

## Example of work

![Example of work GIF](media/exampleOfWork.gif)


# Running the Project

First, clone the repository using the following git command:

```bash
git clone https://github.com/TimurMalanin/dataspell-test-task.git
```

Navigate to the project directory:

```bash
cd dataspell-test-task
```

Run the project:

```bash
./gradlew run
```