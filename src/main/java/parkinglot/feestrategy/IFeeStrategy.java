package parkinglot.parkinglot.feestrategy;

import parkinglot.entities.parkingticket.ParkingTicket;

public interface IFeeStrategy {

    public double calculateFee(ParkingTicket parkingTicket);
}
