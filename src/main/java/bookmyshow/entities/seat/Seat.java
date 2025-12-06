package bookmyshow.entities.seat;

public class Seat {

    String id;
    int row;
    int col;
    SeatType seatType;
    SeatStatus status;

    public Seat(String id, int row, int col, SeatType seatType) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.seatType = seatType;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
