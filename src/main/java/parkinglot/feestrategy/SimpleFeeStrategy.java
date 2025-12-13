package parkinglot.parkinglot.feestrategy;

import parkinglot.entities.parkingticket.ParkingTicket;

import java.time.Duration;

public class SimpleFeeStrategy implements IFeeStrategy {

    private static final int MINIMUM_AMOUNT = 20;
    private static final double HOURLY_RATE = 10;

    @Override
    public double calculateFee(ParkingTicket parkingTicket) {

        long seconds = Duration.between(
                parkingTicket.getEntryTime(),
                parkingTicket.getExitTime()
        ).getSeconds();


        double durationInHours = seconds/3600.0;

        double fee;

        if(durationInHours > 1) {
            fee = (durationInHours - 1) * HOURLY_RATE + MINIMUM_AMOUNT;
        } else {
            fee = MINIMUM_AMOUNT;
        }

        return fee;
    }
}
