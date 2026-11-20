import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {

    final String dbURL = "jdbc:mysql://localhost:3306/JAVA_hotel_reservation_system";
    final String user = "";
    final String password = "";

    public Room getRoom(Connection conn, int roomNumber) throws SQLException{
        String sql = "SELECT * FROM rooms WHERE room_id =?";

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, roomNumber);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int number = rs.getInt("room_id");
                    boolean available = rs.getBoolean("is_available");
                    String category = rs.getString("category");
                    double price = rs.getDouble("price");

                    return new Room(number, available, category, price);
                }
            }
        }

        return null;
    } 

    public Room selectRoom(){
        Scanner scanner = new Scanner(System.in);

        try(Connection conn = DriverManager.getConnection(dbURL, user, password)){
            while(true){
                System.out.println("Please choose the number of the room you would like to book:");
                int selectedRoom = scanner.nextInt();

                Room room = getRoom(conn, selectedRoom);

                if(room == null){
                    System.out.println("This room does not exist. Try again!");
                }else if(room.getIsAvailable()){
                    System.out.println("Room is available!");
                    System.out.println("");
                    return room;
                }else{
                    System.out.println("This room is not available. Try again!");
                    System.out.println("");
                    return null;
                }
            }

        }catch (SQLException e){
            System.out.println("Database error: " + e.getMessage());
            return null;
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
                System.out.println("");
                return fullName;
            }else{
                System.out.println("The name entered in not valid. Try again!");
            }
        }
    }

    public void saveReservation(Connection conn, Reservation reservation) throws SQLException {
        LocalDate date = LocalDate.now();

        String updateSQL = "UPDATE rooms SET is_available = FALSE WHERE room_id = ? AND is_available =TRUE";
        String insertSQL = "INSERT INTO reservations (room_num, date, full_name) VALUES (?, ?, ?)";

        conn.setAutoCommit(false);

        try{
            try(PreparedStatement updateStatement = conn.prepareStatement(updateSQL)){
                updateStatement.setInt(1, reservation.getRoomNumber());

                int rowsUpdated = updateStatement.executeUpdate();

                if(rowsUpdated == 0){
                    conn.rollback();
                    System.out.println("Sorry, room is no longer available or does not exist.");
                    return;
                }else if(rowsUpdated > 0){
                    System.out.println("");
                }
            }

            try(PreparedStatement insertStatement = conn.prepareStatement(insertSQL)){
                insertStatement.setInt(1, reservation.getRoomNumber());
                insertStatement.setDate(2, java.sql.Date.valueOf(reservation.getDate()));
                insertStatement.setString(3, reservation.getFullName());

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
                    Reservation reservation = new Reservation(
                        reservationID,
                        rs.getInt("room_num"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("full_name")
                    );

                    System.out.println("Reservation info:");
                    System.out.println("Reservation ID: " + reservation.getReservationID());
                    System.out.println("Room number: " + reservation.getRoomNumber());
                    System.out.println("Date: " + reservation.getDate());
                    System.out.println("Full name: " + reservation.getFullName());
                }else{
                    System.out.println("Reservation does not exist!");
                }

            }catch (SQLException e){
                System.out.println("Something went wrong: " + e);

            }      
    }

    public void makeReservation(){
        try(Connection conn = DriverManager.getConnection(dbURL, user, password)){

            Room room = selectRoom();

            if(room == null){
                System.out.println("Reservation cancelled.");
                return;
            }

            String customerName = collectUserInfo();
            boolean paymentSuccesful = makePayment(room);

            if(paymentSuccesful){
                Reservation reservation = new Reservation(
                    LocalDate.now(),
                    room.getRoomNumber(),
                    customerName
                );

                saveReservation(conn, reservation);
            }else{
                System.out.println("Reservation cancelled. No reservation was saved.");
            }

            /*if(paymentSuccesful){
                saveReservation(conn, room.getRoomNumber(), customerName);
            }else{
                System.out.println("Reservation cancelled. No reservation was saved.");
            }*/

        }catch(SQLException e){
            System.out.println("Reservation failed: " + e.getMessage());
        }
    }

    public boolean makePayment(Room room){
        try(Connection conn = DriverManager.getConnection(dbURL, user, password)){
            Scanner scanner = new Scanner(System.in);

            System.out.println("To complete the reservation you need to make a payment.");

            double price = room.getPrice();

            System.out.println("The price of this room is: " + price);
            System.out.println("Would you like to make the payment? y/n");

            String choice = scanner.next();

            if(choice.equalsIgnoreCase("y")){
                System.out.println("Please enter your credit card number (8 digits): ");

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
                System.out.println("Thank you! Goodbye!");
                return false;
            }else{
                System.out.println("Incorrect input!");
                return false;
            }


        }catch(SQLException e){
            System.out.println("Payment failed: " + e.getMessage());
            return false;
        }
    }

   
}
