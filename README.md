# Kotlin Swing Expression Evaluator

## Introduction

This application is a Kotlin-based desktop tool that uses Swing to create a user-friendly interface similar to Excel for inputting and evaluating mathematical expressions. It allows users to enter expressions in a spreadsheet-like format and computes results in real time.

## Features

- **Graphical User Interface:** Built with Swing, offering a spreadsheet-like experience.
- **Expression Parsing:** Converts standard mathematical expressions from infix to postfix format.
- **Real-Time Computation:** Evaluates expressions as they are entered.
- **Supported Operations:**
    - **Unary Operators:** Negation and Square Root.
    - **Binary Operators:** Addition, Subtraction, Multiplication, Division, and Power.
- **Function Support:** Includes functions for common mathematical operations such as square root.
- **Error Handling:** Provides error feedback for invalid expressions.

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


# Project Setup Instructions

Follow these instructions to set up the project locally. These steps will guide you through cloning the repository, creating and switching to a new branch, and pulling the latest changes from the development branch.

## Cloning the Repository

First, clone the repository using the following git command:

```bash
git clone https://github.com/TimurMalanin/dataspell-test-task.git
```

Navigate to the project directory:

```bash
cd dataspell-test-task
```

## Setting Up the Branch

Once inside the project directory, create and switch to a new branch named test:

```bash
git checkout -b test
```

Set the upstream branch to `development` to track changes from the development branch:

```bash
git branch --set-upstream-to=origin/development test
```

## Pulling Latest Updates

Finally, pull the latest changes from the development branch:

```bash
git pull
```

# Running the Project

Building the project

```bash
./gradlew build
```

Run the project

```bash
./gradlew run
```