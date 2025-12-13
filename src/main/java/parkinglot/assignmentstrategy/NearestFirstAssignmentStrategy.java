package parkinglot.parkinglot.assignmentstrategy;

import parkinglot.entities.parkingfloor.ParkingFloor;
import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.vehicle.Vehicle;

import java.util.List;

public class NearestFirstAssignmentStrategy implements IAssignmentStrategy {

    @Override
    public ParkingSpot findParkingSpot(List<ParkingFloor> floors, Vehicle v) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(v);
            if (spot != null) {
                return spot;
            }
        }

        return null;
    }


}
