package ExtraCreditProject1;

/**
 * @author Nicholas Alpert
 * @version 1/25/2023
 * Extra Credit University project Driver
 */
public class Driver {
    public static void main(String args[]) throws Exception {

        School test1 = new School("Rowan University", "123 Fake Address");
        test1.enrollStudents();
        test1.getStudents();

        Student john = School.students.get("6754");
        john.addClass("Linear Algebra", "Mike Evans");
        john.addClass("DAA", "Colin Brown");
        john.printSchedule();
    }
}