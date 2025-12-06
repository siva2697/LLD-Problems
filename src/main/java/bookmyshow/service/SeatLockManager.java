package bookmyshow.service;

import bookmyshow.entities.Show;
import bookmyshow.entities.User;
import bookmyshow.entities.seat.Seat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeatLockManager {

    private static final long LOCK_TIMEOUT_MS = 500; // Ideally it would be in minutes
    private final ConcurrentHashMap<String, Object> showLocks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> lockedSeats = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public boolean lockSeats(List<Seat> seats, Show show, User user) {

        // TODO: Check if show is null

        Object lock = showLocks.computeIfAbsent(show.getId(), id -> new Object());

        synchronized (lock) {

            // 1. Check if all seats are available
            for (Seat seat: seats) {
                // 1.1 Check if seat is already booked
                if (show.isSeatBooked(seat.getId())) {
                    System.out.println("Seat " + seat.getId() + " is not available");
                    return false;
                }

                // 1.2 Check if seat is already locked
                if(lockedSeats.containsKey(show.getId()) &&
                   lockedSeats.get(show.getId()).containsKey(seat.getId())
                ) {
                    System.out.println("Seat " + seat.getId() + " is already locked");
                    return false;
                }

            }

            // 2. Lock Seats

            lockedSeats.computeIfAbsent(show.getId(), k -> new ConcurrentHashMap<>());
            for (Seat seat : seats) {
                lockedSeats.get(show.getId()).put(seat.getId(), user.getId());
            }

            scheduler.schedule(() -> unlockSeats(seats, show, user), LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            System.out.println("Locked seats: " + seats + " for user " + user.getId());
        }
        return true;
    }

    public void unlockSeats(List<Seat> seats, Show show, User user) {

        Object lock = showLocks.get(show.getId());

        if(lock == null) {
            // TODO: Handle this scenario
            return;
        }

        synchronized (lock) {

            Map<String, String> lockedSeatsOfShow = lockedSeats.get(show.getId());

            // No seats locked for this show
            if(lockedSeatsOfShow == null) {
                return;
            }

            for(Seat seat: seats) {

                // Still locked by the same user
                if(
                    lockedSeatsOfShow.containsKey(seat.getId()) &&
                    lockedSeatsOfShow.get(seat.getId()).equals(user.getId())
                ) {
                    lockedSeatsOfShow.remove(seat.getId());
                    if(show.isSeatBooked(seat.getId())) {
                        System.out.println("Unlocked seat: " + seat.getId() + " due to booking completion");
                    } else {
                        System.out.println("Unlocked seat: " + seat.getId() + " due to timeout.");
                    }
                }
            }
        }
    }
}
