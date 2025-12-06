package bookmyshow.service;

import bookmyshow.entities.Show;
import bookmyshow.entities.Ticket;
import bookmyshow.entities.User;
import bookmyshow.entities.seat.Seat;
import bookmyshow.entities.seat.SeatStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingManager {

    SeatLockManager seatLockManager;

    public BookingManager() {
        seatLockManager = new SeatLockManager();
    }

    public Ticket bookTickets(List<Seat> seats, Show show, User user) {

        // 1. Lock Seats
        boolean locked = seatLockManager.lockSeats(seats, show, user);

        if(!locked) {
            System.out.println("Failed to lock seats");
            return null;
        }

        // 2. Calculate Price
        double amount = show.calculatePrice(seats);

        // 3. Process payment
        boolean paymentSuccessful = Math.random() > 0.20;

        if (paymentSuccessful) {
            Ticket ticket = new Ticket.TicketBuilder(UUID.randomUUID().toString())
                    .setSeats(seats)
                    .setShow(show)
                    .setUser(user)
                    .build();

            // 4. Confirm Booking - Mark Seats as BOOKED
            show.bookSeats(seats);

            seatLockManager.unlockSeats(seats, show, user);

            return ticket;

        } else {
            System.out.println("Payment Failed");
            return null;
        }
    }
}
