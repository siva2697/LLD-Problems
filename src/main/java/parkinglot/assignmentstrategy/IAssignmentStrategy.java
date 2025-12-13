package parkinglot.parkinglot.assignmentstrategy;

import parkinglot.entities.parkingfloor.ParkingFloor;
import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.vehicle.Vehicle;

import java.util.List;

public interface IAssignmentStrategy {

    public ParkingSpot findParkingSpot(List<ParkingFloor> floors, Vehicle v);

}
