package bookmyshow.entities;

import bookmyshow.entities.seat.Seat;
import bookmyshow.pricing.IPricingStrategy;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Show {

    String id;
    Movie movie;
    Screen screen;
    Instant startTime;
    IPricingStrategy pricingStrategy;
    Map<String, Boolean> bookedSeats;

    public Show(String id, Movie movie, Instant startTime, Screen screen) {
        this.id = id;
        this.movie = movie;
        this.startTime = startTime;
        this.screen = screen;
        bookedSeats = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public IPricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setPricingStrategy(IPricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculatePrice(List<Seat> seats) {
      return this.pricingStrategy.calculatePrice(seats, screen.getBasePricing());
    }

    public List<Seat> getSeats() {
        return this.getScreen().getSeats();
    }

    public boolean isSeatBooked(String seatId) {
        return bookedSeats.getOrDefault(seatId, false);
    }

    public void bookSeats(List<Seat> seats) {
        for(Seat seat: seats) {
            bookedSeats.put(seat.getId(), true);
        }
    }

}
