package parkinglot.entities.parkingfloor;

import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.parkingspot.ParkingSpotSize;
import parkinglot.entities.vehicle.Vehicle;
import parkinglot.entities.vehicle.VehicleSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingFloor {

    private final int floorNumber;
    private final Map<String, ParkingSpot> parkingSpots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.parkingSpots = new ConcurrentHashMap<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void addParkingSpot(ParkingSpot spot) {
        parkingSpots.put(spot.getSpotID(), spot);
    }

    public ParkingSpot findAvailableSpot(Vehicle vehicle) {

        for(ParkingSpot spot: parkingSpots.values()) {
            if (spot.canFitSize(vehicle) && !spot.isOccupied()) {
                return spot;
            }
        }

        return null;
    }

    public void displayAvailability() {

        System.out.println("Spot Availability");

        Map<ParkingSpotSize, Integer> parkingSpotCounts = new HashMap<>();

        for(ParkingSpotSize size: ParkingSpotSize.values()) {
            parkingSpotCounts.put(size, 0);
        }

        for(ParkingSpot spot: parkingSpots.values()) {
            ParkingSpotSize spotSize = spot.getSpotSize();
            boolean isOccupied = spot.isOccupied();
            if (!isOccupied) {
                parkingSpotCounts.put(spotSize, parkingSpotCounts.get(spotSize) + 1);
            }
        }

        parkingSpotCounts.forEach((size, count) -> System.out.println(size + " --> " + count));
    }

    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingSpots.values());
    }
}
