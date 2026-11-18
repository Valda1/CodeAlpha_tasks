import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    DB myData = new DB();    
    Scanner scanner = new Scanner(System.in);
    int choice = 0;
   
    System.out.println("Welcome to our hotel!");
    System.out.println("______________________");
    System.out.println("What would you like to do?");
    printOptions();
   
    choice = scanner.nextInt();
   
    switch(choice) {
        case 1:
            myData.makeReservation();
            break;
        case 2:
            myData.cancelReservation();
            break;
        case 3:
            myData.showReservation();
            break;
        case 4:
            myData.selectRoom();
            break;
    }
}
   
    public static void printOptions () {
        System.out.println("\nPress");
        System.out.println("\t 1 - Make a reservation");
        System.out.println("\t 2 - Cancel my reservation");
        System.out.println("\t 3 - Check my reservation info");
        System.out.println("\t 4 - Check room's availability");
    }
  
}


