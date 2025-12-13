package parkinglot;

import parkinglot.assignmentstrategy.FarthestFirstAssignmentStrategy;
import parkinglot.assignmentstrategy.IAssignmentStrategy;
import parkinglot.assignmentstrategy.NearestFirstAssignmentStrategy;
import parkinglot.entities.parkingfloor.ParkingFloor;
import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.parkingspot.ParkingSpotSize;
import parkinglot.entities.parkingticket.ParkingTicket;
import parkinglot.entities.vehicle.Bike;
import parkinglot.entities.vehicle.Car;
import parkinglot.entities.vehicle.Truck;
import parkinglot.entities.vehicle.Vehicle;
import parkinglot.feestrategy.IFeeStrategy;
import parkinglot.feestrategy.SimpleFeeStrategy;
import parkinglot.parkinglotmanager.ParkingLotManager;

import java.time.Duration;
import java.time.Instant;

public class ParkingLotDemo {

    public static void main(String[] args) {


        ParkingLotManager parkingLotManager = ParkingLotManager.getInstance();

        IAssignmentStrategy assignmentStrategy = new NearestFirstAssignmentStrategy();
        IFeeStrategy feeStrategy = new SimpleFeeStrategy();

        // 1. Policies
        parkingLotManager.setAssignmentStrategy(assignmentStrategy);
        parkingLotManager.setFeeStrategy(feeStrategy);


        // 2. Add Floors
        setupFloors(parkingLotManager);

        // 3. Simulate Usecases:
        simulateParkingLot(parkingLotManager);


    }


    private static void setupFloors(ParkingLotManager parkingLotManager) {

        // Constructing Floors

        ParkingFloor parkingFloor1 = new ParkingFloor(1);
        ParkingFloor parkingFloor2 = new ParkingFloor(2);
        ParkingFloor parkingFloor3 = new ParkingFloor(3);

        ParkingSpot spot1 = new ParkingSpot("spot1", ParkingSpotSize.SMALL);
        ParkingSpot spot2 = new ParkingSpot("spot2", ParkingSpotSize.MEDIUM);
        ParkingSpot spot3 = new ParkingSpot("spot3", ParkingSpotSize.LARGE);

        parkingFloor1.addParkingSpot(spot1);
        parkingFloor1.addParkingSpot(spot2);
        parkingFloor1.addParkingSpot(spot3);

        parkingLotManager.registerFloor(parkingFloor1);


        ParkingSpot spot4 = new ParkingSpot("spot4", ParkingSpotSize.SMALL);
        ParkingSpot spot5 = new ParkingSpot("spot5", ParkingSpotSize.SMALL);
        ParkingSpot spot6 = new ParkingSpot("spot6", ParkingSpotSize.SMALL);

        parkingFloor2.addParkingSpot(spot4);
        parkingFloor2.addParkingSpot(spot5);
        parkingFloor2.addParkingSpot(spot6);

        parkingLotManager.registerFloor(parkingFloor2);


        ParkingSpot spot7 = new ParkingSpot("spot7", ParkingSpotSize.SMALL);
        ParkingSpot spot8 = new ParkingSpot("spot8", ParkingSpotSize.MEDIUM);
        ParkingSpot spot9 = new ParkingSpot("spot9", ParkingSpotSize.LARGE);

        parkingFloor3.addParkingSpot(spot7);
        parkingFloor3.addParkingSpot(spot8);
        parkingFloor3.addParkingSpot(spot9);

        parkingLotManager.registerFloor(parkingFloor3);

    }

    private static void simulateParkingLot(ParkingLotManager parkingLotManager) {

        // Constructing Vehicles

        Vehicle bike1 = new Bike("BIKE 11111");
        Vehicle bike2 = new Bike("BIKE 11111");
        Vehicle bike3 = new Bike("BIKE 11111");
        Vehicle bike4 = new Bike("BIKE 11111");

        Vehicle car1 = new Car("CAR 200001");
        Vehicle car2 = new Car("CAR 200002");
        Vehicle car3 = new Car("CAR 200003");
        Vehicle car4 = new Car("CAR 200004");

        Vehicle truck1 = new Truck("TRUCK 3000001");
        Vehicle truck2 = new Truck("TRUCK 3000002");
        Vehicle truck3 = new Truck("TRUCK 3000003");
        Vehicle truck4 = new Truck("TRUCK 3000004");


        // 1. Assignment with
        ParkingTicket t1 = parkingLotManager.parkVehicle(truck1);
        ParkingTicket t2 = parkingLotManager.parkVehicle(truck2);

        // 2. Parking Lot Full
        ParkingTicket t3 = parkingLotManager.parkVehicle(truck3);
        if(t3 == null) {
            System.out.println("ParkingLot is full");
        }

        // 3. Unpark vehicles

        t1.setExitTime(Instant.now().plus(Duration.ofHours(3)));
        double fee1 = parkingLotManager.unparkVehicle(t1.getTicketID());
        System.out.println("The amount charged is: " + fee1);

        t2.setExitTime(Instant.now().plus(Duration.ofHours(6)));
        double fee2 = parkingLotManager.unparkVehicle(t2.getTicketID());
        System.out.println("The amount charged is: " + fee2);


        // 4. Farthest Strategy
        parkingLotManager.setAssignmentStrategy(new FarthestFirstAssignmentStrategy());

        ParkingTicket t4 = parkingLotManager.parkVehicle(bike1);
        System.out.println(t4.getSpotID());
    }


}
