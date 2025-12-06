package bookmyshow.pricing;

import bookmyshow.entities.seat.Seat;
import bookmyshow.entities.seat.SeatType;

import java.util.List;
import java.util.Map;

public interface IPricingStrategy {
    public double calculatePrice(List<Seat> seats, Map<SeatType, Double> priceMap);
}
