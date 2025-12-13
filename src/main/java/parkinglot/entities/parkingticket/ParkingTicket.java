package parkinglot.entities.parkingticket;

import java.time.Instant;

public class ParkingTicket {

    // Required
    String ticketID;
    String vehicleID;
    String spotID;

    // Optional
    Instant entryTime;
    Instant exitTime;

    private ParkingTicket(
            String ticketID,
            String vehicleID,
            String spotID,
            Instant entryTime,
            Instant exitTime
    ) {
        this.ticketID = ticketID;
        this.vehicleID = vehicleID;
        this.spotID = spotID;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }


    public String getSpotID() {
        return spotID;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public Instant getExitTime() {
        return exitTime;
    }

    public void setExitTime(Instant exitTime) {
        this.exitTime = exitTime;
    }

    public void setEntryTime(Instant entryTime) {
        this.entryTime = entryTime;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public static class PublicTicketBuilder {

        String ticketID;
        Instant entryTime;
        Instant exitTime;
        String vehicleID;
        String spotID;

        public PublicTicketBuilder(String ticketID, String vehicleID, String spotID) {

            // TODO: Add validation checks
            this.ticketID = ticketID;
            this.vehicleID = vehicleID;
            this.spotID = spotID;
        }

        public PublicTicketBuilder setEntryTime(Instant entryTime) {
            this.entryTime = entryTime;
            return this;
        }

        public PublicTicketBuilder setExitTime(Instant exitTime) {
            this.exitTime = exitTime;
            return this;
        }

        public ParkingTicket build() {

            return new ParkingTicket(
                    this.ticketID,
                    this.vehicleID,
                    this.spotID,
                    this.entryTime,
                    this.exitTime
            );
        }

    }




}
