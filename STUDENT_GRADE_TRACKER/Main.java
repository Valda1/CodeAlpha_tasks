import java.util.ArrayList;
import java.util.Scanner;

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

        System.out.println("Welcome to our Student Grade Tracker System!");
        System.out.println("______________________");
        System.out.println("What would you like to do?");
        printOptions();

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch(choice){
            case 1:
                double average1 = getAverageBySubject(students);

                if(average1 != -1){
                    System.out.println("Average grade: " + average1); 
                }

                break;
            case 2:
                double average2 = getAverageForStudent(students);

                if(average2 != -1){
                    System.out.println("Average grade: " + average2);
                }

                break;
            case 3:
                double average3 = getAverageForSubject(students);
                System.out.println("Average grade: " + average3);
                break;
    }

       //double averageBySubjectForSingleStudent = getAverageBySubject(students);
       //System.out.println("Average grade: " + averageBySubjectForSingleStudent);

       //double averageForStudent = getAverageForStudent(students);
       //System.out.println("Average grade for this student: " + averageForStudent);

       //double averageForSubject = getAverageForSubject(students);
       //System.out.println("Average grade for this subject: " + averageForSubject);

    }

        public static void printOptions () {
            System.out.println("\nPress");
            System.out.println("\t 1 - Calculate average grade of a student in a specific subject");
            System.out.println("\t 2 - Calculate average grade of a student across all subjects");
            System.out.println("\t 3 - Calculate average grade of all students in a specific subject");
    }

        public static double getAverageBySubject(ArrayList<Student> students){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the student's ID: ");
            int studentID = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline
            System.out.println("Enter the subject: ");
            String subject = scanner.next();

            double sum = 0;
            int count = 0;

            boolean studentExists = false;
            boolean subjectExists = false;

            for(int i = 0; i < students.size(); i++){
                Student student = students.get(i);

                if(student.getStudentID() == studentID){
                    studentExists = true;
                }

                if(student.getSubject().equalsIgnoreCase(subject)){
                    subjectExists = true;
                }

                if(student.getStudentID() == studentID && student.getSubject().equals(subject)){
                    sum = sum + student.getGrade();
                    count = count +1;
                }
            }

            if(!studentExists){
                System.out.println("This student ID does not exist! Try again!");
            }

            if(!subjectExists){
                System.out.println("This subject does not exist! Try again!");
            }

            if(count == 0){
                return -1;
            }

            return sum / count;

        }

        public static double getAverageForStudent(ArrayList<Student> students){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the student's ID: ");
            int studentID = scanner.nextInt();

            double sum = 0;
            int count = 0;

            boolean studentExists = false;

            for(int i = 0; i < students.size(); i++){
                Student student = students.get(i);

                if(student.getStudentID() == studentID){
                    studentExists = true;

                    sum = sum + student.getGrade();
                    count++;
                }
            } 

            if(!studentExists){
                System.out.println("This student ID does not exist! Try again!");
                return -1;
            }

            return sum / count;

        }

        public static double getAverageForSubject(ArrayList<Student> students){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the subject: ");
            String subject = scanner.next();

            double sum = 0;
            int count = 0;

            for(int i = 0; i < students.size(); i++){
                Student student = students.get(i);

                if(student.getSubject().equals(subject)){
                    sum = sum + student.getGrade();
                    count++;
                }
            }

            if(count == 0){
                System.out.println("This subject does not exist!");
                return 0;
            }else{
                return sum / count;
            }

        }
    }
