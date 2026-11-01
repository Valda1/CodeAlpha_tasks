import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
       
       ArrayList<Student> students = new ArrayList<>();
       
       students.add(new Student(101, "Anna Johnson", "Math", 9.5));
       students.add(new Student(103, "Maria Thompson", "Math", 7.5));
       students.add(new Student(102, "John Doe", "Math", 7.0));

       students.add(new Student(101, "Anna Johnson", "English", 6.5));
       students.add(new Student(103, "Maria Thompson", "English", 5.0));
       students.add(new Student(102, "John Doe", "English", 8.0));

       students.add(new Student(101, "Anna Johnson", "History", 4.6));
       students.add(new Student(103, "Maria Thompson", "History", 3.0));
       students.add(new Student(102, "John Doe", "History", 9.2));
       
       //System.out.println(students.get(0).name);  // Anna
       //System.out.println(students.get(1).grade); // 8.0
   
   
   
    }
}
