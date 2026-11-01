public class Student {

    private int studentID;
    private String fullName;
    private String subject;
    private double grade;
   
    public Student(int studentID, String fullName, String subject, double grade) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.subject = subject;
        this.grade = grade;
    }

    public void setStudentID(int studentID){
        this.studentID = studentID;
    }

    public int getStudentID(){
        return studentID;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }
   
    public String getFullName(){
        return fullName;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    } 

    public void setGrade(double grade){
        this.grade = grade;
    }

    public double getGrade(){
        return grade;
    }


 }
