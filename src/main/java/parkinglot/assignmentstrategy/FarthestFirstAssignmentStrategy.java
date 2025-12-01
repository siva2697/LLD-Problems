package parkinglot.assignmentstrategy;

import parkinglot.entities.parkingfloor.ParkingFloor;
import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.vehicle.Vehicle;

import java.util.List;

public class FarthestFirstAssignmentStrategy implements IAssignmentStrategy {

    @Override
    public ParkingSpot findParkingSpot(List<ParkingFloor> floors, Vehicle v) {

        int numOfFloors = floors.size();

        for(int i=numOfFloors-1; i>=0; i--) {

            ParkingSpot spot = floors.get(i).findAvailableSpot(v);
            if(spot != null) {
                return spot;
            }
        }

        return null;
    }
}
