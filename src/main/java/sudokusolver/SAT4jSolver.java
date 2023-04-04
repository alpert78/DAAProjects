/**
 * SAT4JSolver.java
 * A cnf file is passed to this class. It is then solved by SAT4J. If it is solvable, the correct sudoku puzzle
 * is printed. If not, the puzzle is unsolvable
 *
 * @author Nicholas Alpert
 * @version 4/04/2023
 */
package sudokusolver;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class SAT4jSolver {
    /**
     * Class main method. This is where SAT4J solves the cnf file and if it is solvable, passes the satisfying
     * expression to a method which turns it into the solved puzzle
     * @param args, main methods param
     */
    public static void main(String[] args) {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out, true);
        try {
            IProblem problem = reader.parseInstance(args[0]);
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable! Printing completed sudoku...");
                //reader.decode(problem.model(),out);
                printPuzzle(solver.model());
            } else {
                System.out.println("Unsatisfiable!");
            }
            SudokuToSAT.end = System.nanoTime();
            System.out.println("Runtime: " + (SudokuToSAT.end - SudokuToSAT.start) / 1000000 + " milliseconds");
        } catch (ParseFormatException | IOException e) {
            // TODO Auto-generated catch block
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
    }

    /**
     * If the cnf file is satisfiable, this method will turn the satisfying expression into the solved sudoku
     * puzzle
     * @param solution, an array containing the satisfying expression
     */
    public static void printPuzzle(int[] solution) {
        int[][] puzzle = new int[SudokuToSAT.size][SudokuToSAT.size];
        int row = 0;
        int column = 0;
        int value = 1;
        for (int i = 0; i < solution.length; i++) {
            int temp = solution[i];
            if (temp > 0) {
                puzzle[row][column] = value;
                value++;
                i += SudokuToSAT.size - (column + 1);
                column = 0;
            } else {
                column++;
            }
            if (value == SudokuToSAT.size + 1) {
                row++;
                value = 1;
            }
        }
        for (int i = 0; i < puzzle[0].length; i++) {
            String temp = Arrays.toString(puzzle[i]);
            temp = temp.replaceAll("\\[+", "").replaceAll("]+", "").replaceAll(",", "");
            System.out.println(temp);
        }
    }
}