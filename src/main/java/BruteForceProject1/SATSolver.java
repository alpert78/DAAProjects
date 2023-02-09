package BruteForceProject1;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * SAT Tester
 * Takes in a CNF file as input and reads it, then makes the appropriate variables(equation, amount of vars/clauses, etc.)
 * Makes an ArrayList of all possible combinations of true/false values in binary form
 * @author Nicholas Alpert
 * @version v5 2/6/2023
 */
public class SATSolver {

    public static int numVariables; //Stores the number of variables in the equation
    public static int numClauses; //Stores the number of clauses in the equation

    public static ArrayList<ArrayList<Integer>> equation = new ArrayList<>(); // Stores ArrayLists that store the clauses

    /**
     * Takes in a CNF file and reads it. Sets the number of variables and clauses.
     * Fills in the equation ArrayList with the appropriate clauses for later use
     */
    private static void readCNF(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of your .cnf file...");
        String cnfFileName = sc.nextLine();
        try {
            Scanner sc2 = new Scanner(new File(cnfFileName));
            ArrayList<Integer> temp = new ArrayList<Integer>();
            while(sc2.hasNext()){
                String nextLine = sc2.nextLine().trim(); // Take the next line of the file and trim spaces
                nextLine = nextLine.replaceAll(" +", " ");
                if(((Character) nextLine.charAt(0)).equals('p')){ // If the line is the p cnf line, set variables
                    String[] infoLine = nextLine.split(" ");
                    numVariables = Integer.valueOf(infoLine[2]);
                    numClauses = Integer.valueOf(infoLine[3]);
                }
                if(((Character) nextLine.charAt(0)).equals('-') || Character.isDigit(nextLine.charAt(0))){ // If the line is an equation line
                    String[] current = nextLine.split(" ");
                    if(Integer.valueOf(current[current.length - 1]) == 0 && current.length != 1){ // If the equation is on one line
                        for(int i = 0; i < current.length - 1; i++){
                            temp.add(Integer.valueOf(current[i]));
                        }
                        equation.add(temp);
                        temp = new ArrayList<Integer>();
                    } else if (current.length == 1 && Integer.valueOf(current[0]) == 0) { // If line is only 0
                        equation.add(temp);
                        temp = new ArrayList<Integer>();
                    } else{
                        for(int i = 0; i < current.length; i++){ // If equation is split across different lines
                            temp.add(Integer.valueOf(current[i]));
                        }
                    }
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * Determines if the input equation is SAT or UNSAT
     * @return the ArrayList of correct combo of variables, or UNSAT and an empty ArrayList if UNSAT
     */
    private static ArrayList<Integer> SatChecker(){
        readCNF();
        ArrayList<Integer> current = new ArrayList<>();
        int checked = 0;
        int correct = 0;
        for(int i = 0; i < Math.pow(2, numVariables); i++){
            if(correct == numClauses){
                System.out.println("SAT");
                return current;
            }
            String comboBinary = Integer.toBinaryString(i); // Makes ints 1 - numVariables into binary
            String bits = "%" + numVariables + "s";
            comboBinary = String.format(bits, comboBinary).replace(' ', '0');
            current = binaryToVariableHelper(comboBinary); // Converts binary to appropriate variables
            correct = 0;
            checked = 0;
            for(int j = 0; j < equation.size(); j++){
                if(checked == numVariables){
                    break;
                }
                for(int k = 0; k < current.size(); k++){
                    if(equation.get(j).contains(current.get(k))){
                        checked = 0;
                        correct++;
                        break;
                    }
                    else if (!(equation.get(j).contains(current.get(k)) && checked < numVariables )) {
                        checked++;
                    }
                    else{
                        System.out.println("UNSAT");
                        return new ArrayList<>();
                    }
                }
            }
        }
        System.out.println("UNSAT");
        return new ArrayList<>();
    }

    /**
     * Helper method used in SatChecker. Converts the Binary value to the appropriate (+ or -) variable
     * @param binaryString
     * @return an ArrayList of Integers, in this case, the variables
     */
    private static ArrayList<Integer> binaryToVariableHelper(String binaryString){
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i = 0; i < binaryString.length(); i++){
            if(((Character) binaryString.charAt(i)).equals('1')){
                temp.add(i + 1);
            } else if(((Character) binaryString.charAt(i)).equals('0')){
                temp.add(-(i + 1));
            }
        }
        return temp;
    }

    /**
     * Main method to run all other methods
     * @param args
     */
    public static void main(String[] args){
        try{
            System.out.println(SatChecker());
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }
}