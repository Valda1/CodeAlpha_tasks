public class Room{
    private int roomNumber;
    private boolean isAvailable;
    private String category;
    private double price;

    public Room(int roomNumber, boolean isAvailable, String category, double price){
        this.roomNumber = roomNumber;
        this.isAvailable = isAvailable;
        this.category = category;
        this.price = price;
    }

    public int getRoomNumber(){
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber){
        this.roomNumber = roomNumber;
    }

    public boolean getIsAvailable(){
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}

