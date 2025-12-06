package bookmyshow.entities;

import bookmyshow.entities.seat.Seat;
import bookmyshow.entities.seat.SeatType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen {

    private String id;
    private List<Show> shows;
    private List<Seat> seats;
    private final Map<SeatType, Double> basePricing;

    public Screen(String id) {
        this.id = id;
        shows = new ArrayList<>();
        seats = new ArrayList<>();
        this.basePricing = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public Map<SeatType, Double> getBasePricing() {
        return basePricing;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setBasePricing(Map<SeatType, Double> pricing) {
        this.basePricing.clear();
        this.basePricing.putAll(pricing);
    }

}
