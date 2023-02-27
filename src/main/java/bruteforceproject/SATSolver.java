package bruteforceproject;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * SAT Tester
 * Takes in a CNF file as user input and reads it, then makes the appropriate variables(equation, amount of vars/clauses, etc.)
 * Checks all possible combinations and finds the first one that makes the equation SAT, or it is UNSAT if one is not found
 *
 * @author Nicholas Alpert
 * @version v9 2/27/2023
 */
public class SATSolver {
    public static int numVariables; //Stores the number of variables in the equation
    public static int numClauses; //Stores the number of clauses in the equation
    public static ArrayList<ArrayList<Integer>> equation = new ArrayList<>(); // Stores ArrayLists that store the clauses
    public static boolean lastBinary = false; //Used for while loop control (Needed for special case = All 1's in binaryCombo

    /**
     * Reads the CNF file with a scanner
     * Fills in the equation ArrayList with the appropriate clauses for later use
     */
    private static void readCNF() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of your .cnf file...");
        String cnfFileName = sc.nextLine();
        try {
            Scanner sc2 = new Scanner(new File(cnfFileName));
            ArrayList<Integer> temp = new ArrayList<>();
            while (sc2.hasNext()) {
                String nextLine = sc2.nextLine().trim(); // Take the next line of the file and trim spaces
                nextLine = nextLine.replaceAll(" +", " ");
                if (((Character) nextLine.charAt(0)).equals('p')) { // If the line is the p cnf line, set variables
                    String[] infoLine = nextLine.split(" ");
                    numVariables = Integer.valueOf(infoLine[2]);
                    numClauses = Integer.valueOf(infoLine[3]);
                }
                if (((Character) nextLine.charAt(0)).equals('-') || Character.isDigit(nextLine.charAt(0))) { // If the line is an equation line
                    String[] current = nextLine.split(" ");
                    if (Integer.valueOf(current[current.length - 1]) == 0 && current.length != 1) { // If the equation is on one line
                        for (int i = 0; i < current.length - 1; i++) {
                            temp.add(Integer.valueOf(current[i]));
                        }
                        equation.add(temp);
                        temp = new ArrayList<>();
                    } else if (current.length == 1 && Integer.valueOf(current[0]) == 0) { // If line is only 0
                        if (!temp.isEmpty()) { // Makes sure it only adds if it isn't an empty line
                            equation.add(temp);
                            temp = new ArrayList<>();
                        }
                        temp = new ArrayList<>(); // Doesn't add anything if the line is only 0
                    } else {
                        for (int i = 0; i < current.length; i++) { // If equation is split across different lines
                            temp.add(Integer.valueOf(current[i]));
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Determines if the input equation is SAT or UNSAT
     *
     * @return the ArrayList of correct combo of variables, or UNSAT and an empty ArrayList if UNSAT
     */
    private static ArrayList<Integer> SatChecker() {
        readCNF(); // Reads the CNF file
        ArrayList<Integer> current = new ArrayList<>();
        int checked = 0;
        int correct = 0;
        String comboBinary = "";
        for (int i = 0; i < numVariables; i++) { //Binary starts with all 0's
            comboBinary = comboBinary + "0";
        }
        while (!lastBinary) {
            if (!comboBinary.contains("0")) { //Used for special case where binary is all 1's
                lastBinary = true;
            }
            if (correct == numClauses) { //Equation is satisfiable
                System.out.println("SAT");
                return current;
            }
            current = binaryToVariableHelper(comboBinary); // Converts binary to appropriate variables
            correct = 0;
            checked = 0;
            for (int j = 0; j < equation.size(); j++) {
                if (checked == numVariables) {
                    break;
                }
                for (int k = 0; k < current.size(); k++) {
                    if (equation.get(j).contains(current.get(k))) {
                        checked = 0;
                        correct++;
                        break;
                    } else if (!(equation.get(j).contains(current.get(k)) && checked < numVariables)) {
                        checked++;
                    } else {
                        System.out.println("UNSAT"); //Equation is unsatisfiable
                        return new ArrayList<>();
                    }
                }
            }
            if (comboBinary.contains("0")) {
                comboBinary = binaryAddition(comboBinary);
            }
        }
        if (correct == numClauses) { //Equation is satisfiable (Will only happen when solution is all variables = true
            System.out.println("SAT");
            return current;
        }
        System.out.println("UNSAT"); //Equation is unsatisfiable
        return new ArrayList<>();
    }

    /**
     * Helper method used in SatChecker. Converts the Binary value to the appropriate (+ or -) variable
     *
     * @param binaryString, to be converted into variables
     * @return an ArrayList of Integers, in this case, the variables
     */
    private static ArrayList<Integer> binaryToVariableHelper(String binaryString) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < binaryString.length(); i++) {
            if (((Character) binaryString.charAt(i)).equals('1')) {
                temp.add(i + 1);
            } else if (((Character) binaryString.charAt(i)).equals('0')) {
                temp.add(-(i + 1));
            }
        }
        return temp;
    }

    /**
     * A method to add one to the binary String by switching the last 0 to a 1, and switching the rest to 0s
     *
     * @param comboBinary, a String in binary
     * @return the binary String, plus 1
     */
    private static String binaryAddition(String comboBinary) {
        int lastZero = comboBinary.lastIndexOf('0') + 1;
        StringBuilder build = new StringBuilder(comboBinary);
        build.replace(lastZero - 1, lastZero, "1");
        if (lastZero != comboBinary.length()) {
            build.replace(lastZero, build.length() + 1, "0");
        }
        comboBinary = String.valueOf(build);
        String leadingZeros = "%-" + numVariables + "s";
        comboBinary = String.format(leadingZeros, comboBinary).replace(' ', '0');
        return comboBinary;
    }

    /**
     * Main method to run all other methods
     *
     * @param args, for main method
     */
    public static void main(String[] args) {
        try {
            System.out.println(SatChecker());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}