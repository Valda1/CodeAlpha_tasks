import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {

    final String dbURL = "jdbc:mysql://localhost:3306/JAVA_hotel_reservation_system";
    final String user = "";
    final String password = "";


    public int selectRoom(){
    Scanner scanner = new Scanner(System.in);

    try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {
        while (true) {
            System.out.println("Please choose the number of the room you would like to book:");
            int selectedRoom = scanner.nextInt();

            String sql = "SELECT is_available FROM rooms WHERE room_id = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selectedRoom);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        boolean isAvailable = rs.getBoolean("is_available");

                        if (isAvailable) {
                            System.out.println("Room is available!");
                            System.out.println("");
                            return selectedRoom;
                        } else {
                            System.out.println("This room is not available. Try again!");
                            System.out.println("");
                        }
                    } else {
                        System.out.println("This room does not exist. Try again!");
                    }
                }
            }
        }
    }catch (SQLException e){
        System.out.println("Database error: " + e.getMessage());
        return -1;
    }
}

    public String collectUserInfo(){
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Please enter your full name:");
            String fullName = scanner.nextLine().trim();
            Pattern pattern = Pattern.compile("[a-zA-Z]+(?:[ '-][a-zA-Z]+)*");
            Matcher matcher = pattern.matcher(fullName);

            if(matcher.matches()){
                System.out.println("Name accepted!");
                return fullName;
            }else{
                System.out.println("The name entered in not valid. Try again!");
            }
        }
    }

    public void saveReservation(Connection conn, int roomNumber, String fullName) throws SQLException {
        LocalDate date = LocalDate.now();

        String updateSQL = "UPDATE rooms SET is_available = FALSE WHERE room_id = ? AND is_available =TRUE";
        String insertSQL = "INSERT INTO reservations (room_num, date, full_name) VALUES (?, ?, ?)";

        conn.setAutoCommit(false);

        try{
            try(PreparedStatement updateStatement = conn.prepareStatement(updateSQL)){
                updateStatement.setInt(1, roomNumber);

                int rowsUpdated = updateStatement.executeUpdate();

                if(rowsUpdated == 0){
                    conn.rollback();
                    System.out.println("Sorry, room is no longer available or does not exist.");
                    return;
                }else if(rowsUpdated > 0){
                    conn.commit();
                    System.out.println("");
                }
            }

            try(PreparedStatement insertStatement = conn.prepareStatement(insertSQL)){
                insertStatement.setInt(1, roomNumber);
                insertStatement.setDate(2, java.sql.Date.valueOf(date));
                insertStatement.setString(3, fullName);

                int rowsInserted = insertStatement.executeUpdate();

                if(rowsInserted > 0){
                    conn.commit();
                    System.out.println("You have succefully made a reservation!");
                }else{
                    conn.rollback();
                    System.out.println("Something went wrong! Reservation could not be saved.");
                }

            }catch (SQLException e){
                conn.rollback();
                throw e;
            }

        }finally{
            conn.setAutoCommit(true);
        }
    }

        public void cancelReservation(){
            try(Connection conn = DriverManager.getConnection(dbURL, user, password)){
                Scanner scanner = new Scanner(System.in);

                String selectSQL = "SELECT room_num FROM reservations WHERE reservation_id = ?";
                String deleteSQL = "DELETE FROM reservations WHERE reservation_id = ?";
                String updateSQL = "UPDATE rooms SET is_available = TRUE WHERE room_id = ? AND is_available = FALSE";
                
                System.out.println("Please enter the ID number of your reservation:");
                int reservationID = scanner.nextInt();

                conn.setAutoCommit(false);

                try{
                    int roomNumber;
                    int rowDeleted;

                    try(PreparedStatement ps1 = conn.prepareStatement(selectSQL);){
                        ps1.setInt(1, reservationID);

                        try(ResultSet rs = ps1.executeQuery()){
                            if(!rs.next()){
                                System.out.println("Reservation not found!");
                                conn.rollback();
                                return;
                            }

                            roomNumber = rs.getInt("room_num");
                        }
                    }    

                    try(PreparedStatement ps2 = conn.prepareStatement(deleteSQL)){
                        ps2.setInt(1, reservationID);
                        rowDeleted = ps2.executeUpdate();
                    }
                    
                    if(rowDeleted > 0){
                        try(PreparedStatement ps3 = conn.prepareStatement(updateSQL)){
                            ps3.setInt(1, roomNumber);
                            ps3.executeUpdate();
                        }

                        conn.commit();
                        System.out.println("Your reservation has been cancelled!");
                    }else{
                        conn.rollback();
                        System.out.println("Something went wrong!");
                    }
                }catch(SQLException e){
                    conn.rollback();
                    throw e;
                }finally{
                    conn.setAutoCommit(true);
                }

            }catch(SQLException e){
                System.out.println("Something went wrong: " + e);
            }
        }

        public void showReservation(){
            try(Connection conn = DriverManager.getConnection(dbURL, user, password)){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the ID number of your reservation:");
                int reservationID = scanner.nextInt();

                String selectSQL = "SELECT room_num, date, full_name from reservations WHERE reservation_id = ?";

                PreparedStatement ps = conn.prepareStatement(selectSQL);
                ps.setInt(1, reservationID);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    int roomNumber = rs.getInt(1);
                    Date date = rs.getDate(2);
                    String fullName = rs.getString(3);

                    String output = "Reservation info: \n\t reservation_id: %d \n\t room_id: %d \n\t date: %s \n\t full_name: %s";

                    System.out.println(String.format(output, reservationID, roomNumber, date, fullName));
                }else{
                    System.out.println("Reservation does not exist!");
                }

            }catch (SQLException e){
                System.out.println("Something went wrong: " + e);

            }      
    }

    public void makeReservation(){
        try(Connection conn = DriverManager.getConnection(dbURL, user, password)){

            int roomNumber = selectRoom();
            String customerName = collectUserInfo();
            boolean paymentSuccesful = makePayment(roomNumber);

            if(paymentSuccesful){
                saveReservation(conn, roomNumber, customerName);
            }else{
                System.out.println("Reservation cancelled. No reservation was saved.");
            }

        }catch(SQLException e){
            System.out.println("Reservation failed: " + e.getMessage());
        }
    }

    public boolean makePayment(int roomNumber){
        try(Connection conn = DriverManager.getConnection(dbURL, user, password)){
            Scanner scanner = new Scanner(System.in);

            System.out.println("To complete the reservation you need to make a payment.");

            String sql = "SELECT price FROM rooms WHERE room_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            double price;

            if(rs.next()){
                price = rs.getDouble("price");
                System.out.println("The price of this room is: " + price);
                System.out.println("Would you like to make the payment? y/n");
                String choice = scanner.next();

                if(choice.equalsIgnoreCase("y")){
                    System.out.println("Please enter you credit card number (8 digits): ");
                    String cardNumber = scanner.next();
                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(cardNumber);

                    if(matcher.matches()){
                        System.out.println("Thank you! Your payment has been successful!");
                        return true;
                    }else{
                        System.out.println("Invalid card number. Please try again!");
                        return false;
                    }

                }else if(choice.equalsIgnoreCase("n")){
                    System.out.println("Ok");
                    return false;
                }else{
                    System.out.println("Incorrect input!");
                    return false;
                }

            }else{
                System.out.println("Something went wrong! Please try again!");
                return false;
            }


        }catch(SQLException e){
            System.out.println("Payment failed: " + e.getMessage());
            return false;
        }
    }

   
}
