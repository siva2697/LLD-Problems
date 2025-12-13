package bookmyshow;

import bookmyshow.entities.*;
import bookmyshow.entities.seat.Seat;
import bookmyshow.entities.seat.SeatType;
import bookmyshow.pricing.IPricingStrategy;
import bookmyshow.pricing.MatineePricingStrategy;
import bookmyshow.pricing.RegularPricingStrategy;
import bookmyshow.service.BookingService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class MovieTicketBookingDemo {

    public static void main(String[] args) {

        BookingService service = BookingService.getInstance();

        createUsers(service);
        createMovieEnvironment(service);

        // Fetch Shows
        List<Show> shows = service.findShowsForMovie("Akhanda2", "Bangalore");

        // Fetch Theater
        Theater theater = service.findTheaterByName("Ramesh", "Bangalore");
        System.out.println(theater);

        // Book Seats
        Show selectedShow = shows.get(0);
        List<String> selectedSeatIds1 = List.of(
                selectedShow.getSeats().get(0).getId(),
                selectedShow.getSeats().get(1).getId(),
                selectedShow.getSeats().get(2).getId()
        );
        List<String> selectedSeatIds2 = List.of(
                selectedShow.getSeats().get(1).getId(),
                selectedShow.getSeats().get(2).getId(),
                selectedShow.getSeats().get(3).getId()
        );

        Ticket ticket1 = service.bookTickets(selectedSeatIds1, selectedShow.getId(), "1");
        Ticket ticket2 = service.bookTickets(selectedSeatIds1, selectedShow.getId(), "2");

        System.out.println(shows);

        System.out.println("Completed Demo");
    }

    public static void createUsers(BookingService service) {

        User u1 = new User("1", "Sai Thanuj");
        User u2 = new User("2", "Dheeraj");

        service.addUser(u1);
        service.addUser(u2);

    }

    public static void createMovieEnvironment(BookingService service) {


        Instant morningTime = getInstantInfo(2025, 12, 5, 9, 0);
        Instant eveningTime = getInstantInfo(2025, 12, 5, 18, 0);


        // 1. Create a city
        City city = new City("1", "Bangalore");
        service.addCity(city);

        // 2. Create Movies (2)

        Movie m1 = new Movie("1", "Akhanda2", 175);
        Movie m2 = new Movie("3", "Varanasi", 160);

        service.addMovie(m1);
        service.addMovie(m2);

        // 3. Create Theaters
        Theater rameshTheater = new Theater("1", "Ramesh");

        city.addTheater(rameshTheater);
        service.addTheater(rameshTheater);


        // 3. Create Screens and Seats
        Screen rameshScreen1 = new Screen("Ramesh-Screen-1");

        for(int i = 1; i <= 10; i++) {

            rameshScreen1.addSeat(new Seat("A" + i, 1, i, i<=5 ? SeatType.REGULAR : SeatType.PREMIUM));
            rameshScreen1.addSeat(new Seat("B" + i, 2, i, i<=5 ? SeatType.REGULAR : SeatType.PREMIUM));

        }

        rameshTheater.addScreen(rameshScreen1);

        Show show11 = new Show("1-1", m1, morningTime, rameshScreen1);
        Show show12 = new Show("1-2", m1, eveningTime, rameshScreen1);

        rameshScreen1.addShow(show11);
        rameshScreen1.addShow(show12);
        service.addShow(show11);
        service.addShow(show12);

        Map<SeatType, Double> pricing1 = Map.of(
                SeatType.REGULAR, 100.0,
                SeatType.PREMIUM, 150.0
        );

        rameshScreen1.setBasePricing(pricing1);

        IPricingStrategy regularPricingStrategy = new RegularPricingStrategy();
        IPricingStrategy matineePricingStrategy = new MatineePricingStrategy();

        show11.setPricingStrategy(matineePricingStrategy);
        show12.setPricingStrategy(regularPricingStrategy);

    }

    private static Instant getInstantInfo(int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute)
                .atZone(ZoneId.of("Asia/Kolkata"))
                .toInstant();
    }








}
