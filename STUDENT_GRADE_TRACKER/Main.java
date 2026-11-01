import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        //Student myStudent = new Student(int studentID, String fullName, String subject, double grade);
       
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

       //Iterator<Student> iterator = students.iterator();

       System.out.println(getAverageBySubject(students, "History"));

    }

        public static double getAverageBySubject(ArrayList<Student> students, String subject){
        double sum = 0;
        int count = 0;

        /*for (Student student : students){
            if(student.getSubject().equals(subject)){
                sum += student.getGrade();
                count++;
            }
        }*/

        for(int i = 0; i < students.size(); i++){
            Student student = students.get(i);

            if(student.getSubject().equals(subject)){
                sum = sum + student.getGrade();
                count = count +1;
            }
        }

        if(count == 0){
            return 0;
        }else{
            return sum / count;
        }

        //return count == 0 ? 0 : sum / count;
    }

}
