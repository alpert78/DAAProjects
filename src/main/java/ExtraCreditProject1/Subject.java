package ExtraCreditProject1;

/**
 * @author Nicholas Alpert
 * @version 1/25/2023
 * Represents a subject, or a class, at a School. Each subject has a subject(which class it is; math, science, etc),
 * and a professor
 */
public class Subject {
    private String subject;
    private String professor;

    /**
     * Constructor
     * @param subject
     * @param professor
     */
    public Subject(String subject, String professor){
        this.subject = subject;
        this.professor = professor;
    }

    /**
     * @return this subject's subject
     */
    public String getSubject(){
        return subject;
    }

    /**
     * Set this subject's subject
     * @param subject
     */
    public void setSubject(String subject){
        this.subject = subject;
    }

    /**
     * @return this subject's professor
     */
    public String getProfessor(){
        return professor;
    }

    /**
     * Set this subject's professor
     * @param professor
     */
    public void setProfessor(String professor){
        this.professor = professor;
    }

    /**
     * @return a String representing this subject
     */
    public String toString(){
        return "Subject: " + subject + ", Professor: " + professor + "\n";
    }
}