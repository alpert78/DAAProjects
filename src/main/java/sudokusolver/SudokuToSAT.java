/**
 * SudokuToSAT.java
 * Turns an input sudoku txt file into a corresponding cnf file. The file is than passed to SAT4J to be solved
 * @author Nicholas Alpert
 * @version 3/28/2023
 */
package sudokusolver;

import java.io.*;
import java.util.*;

public class SudokuToSAT {
    public static int size = 0; //size of the puzzle. Example: 9x9 puzzle = 9
    public static int sizeMini = 0;//Square root of size. Used for calculations
    public static Set<Integer> restrictedInts = new HashSet<>();
    public static Set<Integer> restrictedInts2 = new HashSet<>(); //Set of all cells already filled in
    public static long numClause = 0; //Number of clauses in the file
    public static FileWriter fw; //A file writer, to be used with the BufferedWriter

    static {
        try {
            File SATOriginal = new File("SATOriginal.cnf"); //Creates a file to store clauses
            if (SATOriginal.exists()) { //Deletes it if it exists already, to clear it
                SATOriginal.delete();
            }
            SATOriginal.createNewFile(); //Makes the file
            fw = new FileWriter("SATOriginal.cnf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedWriter bw = new BufferedWriter(fw);
    public static File fileName = new File("SATInput.cnf"); //Final cnf file, which will have the clause file appended to it
    public static long start; //Used for runtime
    public static long end; //Used for runtime

    /**
     * Reads the input Sudoku txt file. Fills in size and sizeMini. Also writes the clauses for individual cells
     * already filled in.
     */
    public static void readFile() {
        int column = 0;
        int row = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of your Sudoku file...");
        String cnfFileName = sc.nextLine();
        start = System.nanoTime();
        try {
            Scanner sc2 = new Scanner(new File(cnfFileName));
            while (sc2.hasNext()) {
                String nextLine = sc2.nextLine().trim();
                nextLine = nextLine.replaceAll(" +", " ");
                String[] currentLine = nextLine.split(" ");
                if (currentLine.length == 1) {
                    String tempSize = sc2.nextLine();
                    sizeMini = Integer.parseInt(tempSize);
                    size = sizeMini * sizeMini;
                } else {
                    row++;
                    column = 0;
                    for (int i = 0; i < currentLine.length; i++) {
                        column++;
                        int value = Integer.parseInt(currentLine[i]);
                        if (value != 0) {
                            int temp = (column + ((size * size) * (row - 1)) + (size * (value - 1)));
                            bw.write(temp + " 0");
                            bw.write("\n");
                            numClause++;
                            restrictedInts2.add(-temp);
                        }
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sc.close();
    }

    /**
     * Writes all the row requirements to the cnf file. Example: You must have a (1,2,3...n) in row (1,2,3...n)
     * @throws IOException, File not found
     */
    public static void rowRequirements() throws IOException {
        try {
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 1; i <= size * size * size; i++) {
                bw.write((i) + " ");
                array.add(i);
                if (i % size == 0) {
                    if (i == (size * size * size)) {
                        bw.write("0");
                        bw.write("\n"); //added
                        numClause++;
                        generateAtMost(array, true);
                        array = new ArrayList<>();
                    } else {
                        bw.write("0");
                        bw.write("\n");
                        numClause++;
                        generateAtMost(array, false);
                        array = new ArrayList<>();
                    }
                }
            }
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Writes all the column requirements to the cnf file. Example: You must have a (1,2,3...n) in column (1,2,3...n)
     * @throws IOException, File Not Found
     */
    public static void columnRequirements() throws IOException {
        try {
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 1; i <= size * size; i++) {
                bw.write((i) + " ");
                array.add(i);
                for (int j = i + (size * size); j <= size * size * size; j += size * size) {
                    bw.write((j) + " ");
                    array.add(j);
                }
                bw.write("0");
                bw.write("\n");
                numClause++;
                generateAtMost(array, false);
                array = new ArrayList<>();
            }
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Writes all the cell requirements to the cnf file. Example: You must have a (1,2,3...n) in cell (1,2,3...n^2)
     * @throws IOException, File Not Found
     */
    public static void cellRequirements() throws IOException {
        try {
            for (int k = 0; k < size; k++) {
                for (int i = 1; i <= size; i++) {
                    for (int j = 0; j < size; j++) {
                        int temp = (i + ((size * size) * k)) + (j * size);
                        bw.write(temp + " ");
                        if (i % sizeMini == 1 && k % sizeMini == 0) {
                            restrictedInts.add(temp);
                        }
                    }
                    bw.write("0");
                    bw.write("\n");
                    numClause++;
                }
            }
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * A recursive method that generates all the cell restrictions and writes them to the cnf file. Example:
     * Cell(1,2,3,...n^2) can only have a (1 or 2 or 3 or...n)
     * @param value, int used to generate the written value and keep track of times run
     * @param count, int used to generate the written value and keep track of times run
     * @throws IOException, File Not Found
     */
    public static void cellRestrictions(int value, int count) throws IOException {
        try {
            int temp = value + size;
            for (int i = 0; i < size - (1 + count); i++) {
                bw.write(-value + " " + -(temp + (size * i)) + " 0 \n");
                numClause++;
            }
            if (count == (size - 2)) {
                return;
            }
            count++;
            cellRestrictions(temp, count);
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Writes all the subGrid requirements to the cnf file. Example: You must have a (1,2,3...n) in subGrid (1,2,3...n)
     * @throws IOException, File Not Found
     */
    public static void boxRequirements() throws IOException {
        try {
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 1; i <= size * size * size; i += sizeMini) {
                if (restrictedInts.contains(i)) {
                    bw.write(i + " ");
                    array.add(i);
                    for (int j = 1; j < sizeMini; j++) {
                        bw.write((i + j) + " ");
                        array.add(i+j);
                    }
                    for (int l = 0; l < sizeMini - 1; l++) {
                        for (int k = 0; k < sizeMini; k++) {
                            bw.write(((i + k) + (size * size) + (size * size) * (l)) + " ");
                            array.add(((i + k) + (size * size) + (size * size) * (l)));
                        }
                    }
                    bw.write("0");
                    bw.write("\n");
                    numClause++;
                    generateAtMost(array, false);
                    array = new ArrayList<>();
                }
            }
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Creates a new cnf file, which will be the final one that gets passed to SAT4J. Writes the "p cnf ..."
     * line to it, and appends the previous cnf file with all the clauses to it.
     * @throws IOException, File Not Found
     */
    public static void createFile() throws IOException {
        try {
            File inputFile = new File("SATInput.cnf");
            if (inputFile.exists()) {
                inputFile.delete();
            }
            inputFile.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("SATInput.cnf", true));
            bufferedWriter.write("p cnf " + (size * size * size) + " " + (numClause) + "\n");

            Scanner scanner = new Scanner(new File("SATOriginal.cnf"));

            int counter = 0;
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                counter++;
                if (counter == numClause) {
                    bufferedWriter.write(currentLine);
                } else {
                    bufferedWriter.write(currentLine + "\n");
                }
            }
            bufferedWriter.close();
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Called in each requirement method. Writes the two-variable clauses
     * @param array, an array filled with the line to duplicate with teh clauses
     * @param lastLine, needed to not print a final empty line for SAT4J to work
     * @throws IOException, File Not Found
     */
    public static void generateAtMost(ArrayList<Integer> array, boolean lastLine) throws IOException {
        try {
            for (int i = 0; i < array.size(); i++) {
                for (int j = 1; j < array.size() - i; j++) {
                    bw.write(-array.get(i) + " " + -array.get(i + j) + " 0 ");
                    if(!lastLine){
                       bw.write("\n");
                    }
                    else{
                        if(!(i == size-1)){
                            bw.write("\n");
                        }
                    }
                    numClause++;
                }
            }
        } catch (IOException e){
            throw new FileNotFoundException();
        }
    }

    /**
     * Programs main method. Calls all the requirements/restriction methods and passes the cnf file to SAT4J
     * @param args, main method param
     * @throws IOException, File Not Found
     */
    public static void main(String[] args) throws IOException {
        readFile();
        columnRequirements();
        cellRequirements();
        for (int j = 0; j < size; j++) {
            for (int i = 1; i <= size; i++) {
                cellRestrictions((i + ((size * size) * j)), 0);
            }
        }
        boxRequirements();
        rowRequirements();
        bw.close();
        createFile();
        System.out.println("Write to File Complete");

        String[] solver = new String[1];
        solver[0] = "SATInput.cnf";
        SAT4jSolver.main(solver);

        fileName.deleteOnExit();
    }
}