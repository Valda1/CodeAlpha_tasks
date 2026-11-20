import java.time.LocalDate;

public class Reservation{
    private int reservationID;
    private int roomNumber;
    private LocalDate date;
    private String fullName;

    public Reservation(int reservationID, int roomNumber, LocalDate date, String fullName) {
        this.reservationID = reservationID;
        this.roomNumber = roomNumber;
        this.date = date;
        this.fullName = fullName;
    }

    public void setRoomNumber(int roomNumber){
        this.roomNumber = roomNumber;
    }

    public int getRoomNumber(){
        return roomNumber;
    }

    public void setDate(LocalDate date){
        this.date = date;
    } 

    public LocalDate getDate(){
        return date;
    }
    
    public void setReservationID(int reservationID){
        this.reservationID = reservationID;
    }

    public int getReservationID(){
        return reservationID;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getFullName(){
        return fullName;
    }
   

}
