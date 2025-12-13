package parkinglot.parkinglot.entities.parkingspot;

import parkinglot.entities.vehicle.Vehicle;

public class ParkingSpot {

    private final String spotID;
    private final ParkingSpotSize spotSize;
    private boolean isOccupied;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotID, ParkingSpotSize spotSize) {
        this.spotID = spotID;
        this.isOccupied = false;
        this.parkedVehicle = null;
        this.spotSize = spotSize;
    }

    public String getSpotID() {
        return spotID;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void parkVehicle(Vehicle v) {
        this.parkedVehicle = v;
        this.isOccupied = true;
    }

    public void unparkVehicle() {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }

    public ParkingSpotSize getSpotSize() {
        return spotSize;
    }

    public boolean canFitSize(Vehicle vehicle) {
        switch (vehicle.getSize()) {
            case SMALL:
                return spotSize == ParkingSpotSize.SMALL ||
                        spotSize == ParkingSpotSize.MEDIUM ||
                        spotSize == ParkingSpotSize.LARGE;
            case MEDIUM:
                return spotSize == ParkingSpotSize.MEDIUM ||
                        spotSize == ParkingSpotSize.LARGE;
            case LARGE:
                return spotSize == ParkingSpotSize.LARGE;
            default:
                return false;
        }
    }
}
