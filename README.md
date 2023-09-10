# DAAProjects

# Brute Force Project

This project consists of a single file, `SATSolver.java`, which contains four fields:

- `numVariables`: An integer to hold the number of variables within the input file.
- `numClauses`: An integer to hold the number of clauses within the input equation.
- `equation`: An `ArrayList` of `ArrayLists` of integers to break the equation down into its different clauses.
- `lastBinary`: A boolean which is important for the one case where the only satisfiable answer to the equation is all true variables.

## Methods

### `readCNF()`

This method uses a `Scanner` to read the input `.cnf` file. It will ask the user to input the name or file directory of the `.cnf` file they would like to test. It reads each line to set the fields `numVariables` and `numClauses`. It also adds each clause to the `ArrayList`, accounting for extra spaces and for the case where a single clause is split across multiple lines.

### `SatChecker()`

This is the method where most of the SAT/UNSAT work is done. It starts by calling `readCNF()`. A String is then constructed, made up of `numVariables` 0s. This is to represent the number 0 in binary, with the same number of bits as there are variables. The method `binaryToVariableHelper()` is then called.

### `binaryToVariableHelper()`

This method converts the binary values to the appropriate variables. For example, if there were 4 variables, and we started with 0 in binary (0000), this method would convert this binary value to an `ArrayList` containing the values -1, -2, -3, -4, due to 0s representing NOT, or -value. If the binary input was 0110, this method would return -1, 2, 3, -4. Initially, `Math.pow()` was used to count from 0 to 2^(numVariables). However, this greatly limited the number of variables that the program could handle. With the binary value converted to true or false variables, each clause in the equation is then checked. For an equation to be SAT, each clause must contain at least one variable from the solution set. If a clause doesn't contain any value from the current set of variables, the method `binaryAddition()` is called.

### `binaryAddition(String comboBinary)`

One is added to the binary value by switching the rightmost 0 to 1, and switching each bit after that to 0s. The next binary value is then checked. When a correct solution set is found, "SAT" is printed, and the solution set is returned. If one is not found after searching through all possible combinations, "UNSAT" is printed, and an empty `ArrayList` is returned. This is all started from the `main` method.

### `main(String[] args)`

The main method, which initially prints and calls `SatChecker()`.

# Sudoku Project

This project consists of two files:

1. `SudokuToSat.java`: Responsible for taking in the sudoku text file and turning it into a corresponding CNF file. It does this by writing 5 sets of clauses; single variable clauses that represent the cells already filled in and cell, row, column, and sub-box requirements and restrictions.

2. SAT4JSolver: Solves the CNF file, and if it is satisfiable, a method is called which will turn the satisfying argument into the completed sudoku puzzle. If the CNF file is unsatisfiable, that means the puzzle is not solvable, and a message implying so is printed.

