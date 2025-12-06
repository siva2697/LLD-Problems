package bookmyshow.entities;

import bookmyshow.entities.seat.Seat;

import java.util.List;

public class Ticket {

    String id;
    Show show;
    Movie movie;
    User user;
    List<Seat> seats;

    private Ticket(String id, Show show, User bookedBy, List<Seat> seats) {
        this.id = id;
        this.show = show;
        this.user = bookedBy;
        this.seats = seats;
    }

    public String getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }

    public List<Seat> getSeats() {
        return this.seats;
    }

    public static class TicketBuilder {

        String id;
        Show show;
        User user;
        List<Seat> seats;

        public TicketBuilder(String id) {
            this.id = id;
        }

        public TicketBuilder setShow(Show show) {
            this.show = show;
            return this;
        }

        public TicketBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public TicketBuilder setSeats(List<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public Ticket build() {

            return new Ticket(this.id, this.show, this.user, this.seats);

        }
















    }





}
