package ExtraCreditProject1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Nicholas Alpert
 * @version 1/25/2023
 * Represents a virtual school. A school has a name, an address, and a HashMap of Students and their Ids
 */
public class School {
    private String name;
    private String address;
    public static HashMap<String, Student> students;

    /**
     * Constructor
     * @param name
     * @param address
     */
    public School(String name, String address){
        this.name = name;
        this.address = address;
        students = new HashMap<String, Student>();
    }

    /**
     * @return this school's name
     */
    public String getName(){
        return name;
    }

    /**
     * Set this school's name
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return this school's address
     */
    public String getAddress(){
        return address;
    }

    /**
     * Set this school's address
     * @param address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Enrolls students in this school
     * Creates a buffered reader to read from a csv file of incoming students and their ids
     * Adds them to the HashMap
     * @throws Exception
     */
    public void enrollStudents() throws Exception{
        try {
            BufferedReader br = new BufferedReader(new FileReader("Students.csv"));
            String string = null;
            while ((string = br.readLine()) != null) {
                String[] list = string.split(",");
                students.put(list[1], new Student(list[0], list[1]));
            }
        }
        catch (Exception exception){
            exception.getCause();
        }
    }

    /**
     * Prints the current students enrolled in this school by iterating through the HashMap
     * Prints both Student names and Ids
     */
    public void getStudents(){
        System.out.println("Students currently enrolled at " + this.name + "...");
        if(students.isEmpty()){
            System.out.println("No students currently enrolled");
        }
        Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }
}