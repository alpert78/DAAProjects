package ExtraCreditProject1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Nicholas Alpert
 * @version 1/25/2023
 * Represents a Student. A Student has a name, an id, and a LinkedList containing the classes they are currently
 * enrolled in
 */

public class Student {
    private String name;
    private String id;
    private LinkedList<Subject> classes;

    /**
     * Constructor
     * @param name
     * @param id
     */
    public Student(String name, String id){
        this.name = name;
        this.id = id;
        classes = new LinkedList<Subject>();
    }

    /**
     * @return this Student's name
     */
    public String getName(){
        return name;
    }

    /**
     * Set this Student's name
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return this Student's id
     */
    public String getId(){
        return id;
    }

    /**
     * Set this Student's id
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return a String representing this Student
     */
    public String toString(){
        return name;
    }

    /**
     * Enroll this student in a class with specified professor
     * @param subject
     * @param professor
     */
    public void addClass(String subject, String professor){
        this.classes.add(new Subject(subject, professor));
    }

    /**
     * Prints this Student's current schedule. Uses a buffered writer to iterate through their list of classes and
     * write them to a file called Schedules.txt
     * @throws Exception
     */
    public void printSchedule() throws Exception {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\DAA\\DAAProjects\\Schedules.txt"));
            bw.write("Student's Schedule: \n" + name + "\n");
            Iterator<Subject> it = classes.iterator();
            while (it.hasNext()) {
                bw.write(String.valueOf(it.next()));
            }
            System.out.println("File written to successfully");
            bw.close();
        }
        catch (Exception exception){
            exception.getCause();
        }
    }
}