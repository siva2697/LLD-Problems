package bookmyshow.pricing;

import bookmyshow.entities.seat.Seat;
import bookmyshow.entities.seat.SeatType;

import java.util.List;
import java.util.Map;

public class RegularPricingStrategy implements IPricingStrategy {

    private final double REGULAR_PRICE_MULTIPLIER = 1.0;

    @Override
    public double calculatePrice(List<Seat> seats, Map<SeatType, Double> priceMap) {

        double totalPrice = 0;

        for(Seat seat: seats) {
            totalPrice += priceMap.get(seat.getSeatType()) * REGULAR_PRICE_MULTIPLIER;
        }

        return totalPrice;
    }
}
