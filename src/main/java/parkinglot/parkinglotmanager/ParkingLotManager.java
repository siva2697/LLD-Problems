package parkinglot.parkinglot.parkinglotmanager;

import parkinglot.assignmentstrategy.IAssignmentStrategy;
import parkinglot.entities.parkingfloor.ParkingFloor;
import parkinglot.entities.parkingspot.ParkingSpot;
import parkinglot.entities.parkingticket.ParkingTicket;
import parkinglot.entities.vehicle.Vehicle;
import parkinglot.feestrategy.IFeeStrategy;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotManager {

    private static volatile ParkingLotManager instance;

    private final Map<String, ParkingTicket> activeTickets;

    List<ParkingFloor> parkingFloors;
    private final Map<String, ParkingSpot> spotsByID;

    private IFeeStrategy feeStrategy;
    private IAssignmentStrategy assignmentStrategy;


    private ParkingLotManager() {
        this.activeTickets = new ConcurrentHashMap<>();
        this.parkingFloors = new ArrayList<>();
        this.spotsByID = new HashMap<>();
    }

    public void setAssignmentStrategy(IAssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }

    public void setFeeStrategy(IFeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public static ParkingLotManager getInstance() {
        if(instance == null) {
            synchronized (ParkingLotManager.class) {
                if(instance == null) {
                    instance = new ParkingLotManager();
                }
            }
        }
        return instance;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = assignmentStrategy.findParkingSpot(parkingFloors, vehicle);

        if(spot == null) {
            System.out.println("Spt unavailable");
            return null;
        }

        spot.parkVehicle(vehicle);

        String ticketID = UUID.randomUUID().toString();
        Instant entryTime = Instant.now();

        ParkingTicket parkingTicket = new ParkingTicket.PublicTicketBuilder(
                ticketID,
                vehicle.getLicenseNumber(),
                spot.getSpotID()
                ).setEntryTime(
                    entryTime
                ).build();

        activeTickets.put(parkingTicket.getTicketID(), parkingTicket);

        return parkingTicket;
    }

    public double unparkVehicle(String ticketID) {

        ParkingTicket parkingTicket = activeTickets.get(ticketID);
        if(parkingTicket == null) {
            System.out.println("Ticket not found");
            return 0;
        }

        parkingTicket.setEntryTime(Instant.now());

        ParkingSpot parkingSpot = spotsByID.get(parkingTicket.getSpotID());
        parkingSpot.unparkVehicle();

        double fee = feeStrategy.calculateFee(parkingTicket);

        return fee;
    }

    public void registerFloor(ParkingFloor parkingFloor) {

        this.parkingFloors.add(parkingFloor);

        for(ParkingSpot spot: parkingFloor.getParkingSpots()) {
            spotsByID.put(spot.getSpotID(), spot);
        }
    }

}
