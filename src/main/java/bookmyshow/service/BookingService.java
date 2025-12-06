package bookmyshow.service;

import bookmyshow.entities.*;
import bookmyshow.entities.seat.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {

    private static volatile BookingService instance;

    private final BookingManager bookingManager;

    Map<String, User> users;
    Map<String, City> cities;
    Map<String, Show> shows;
    Map<String, Movie> movies;
    Map<String, Theater> theaters;

    private BookingService() {
        users = new ConcurrentHashMap<>();
        cities = new ConcurrentHashMap<>();
        shows = new ConcurrentHashMap<>();
        movies = new ConcurrentHashMap<>();
        theaters = new ConcurrentHashMap<>();
        bookingManager = new BookingManager();
    }

    public static BookingService getInstance() {
        if(instance == null) {
            synchronized (BookingService.class) {
                if(instance == null) {
                    instance = new BookingService();
                }
            }
        }
        return instance;
    }

    public List<Show> findShowsForMovie(String movieTitle, String cityName) {

        List<Show> shows = new ArrayList<>();

        City city = findCityByCityName(cityName);

        for(Theater theater: city.getTheaters()) {
            for(Screen screen: theater.getScreens()) {
                for(Show show: screen.getShows()) {
                    if(show.getMovie().getTitle().equals(movieTitle)) {
                        shows.add(show);
                    }
                }
            }
        }

        return shows;
    }

    public Theater findTheaterByName(String theaterName, String cityName) {

        City city = findCityByCityName(cityName);

        for(Theater theater: city.getTheaters()) {
            if(theater.getName().equals(theaterName)) {
                return theater;
            }
        }

        return null;
    }

    private City findCityByCityName(String cityName) {
        for(City city: cities.values()) {
            if(city.getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }


    private Seat findSeatBySeatId(Show show, String seatId) {
        for(Seat seat: show.getScreen().getSeats()) {
            if(seat.getId().equals(seatId)) {
                return seat;
            }
        }
        return null;
    }

    public Ticket bookTickets(List<String> seatIds, String showId, String userId) {

        Show show = shows.get(showId);
        User user = users.get(userId);
        List<Seat> seats = new ArrayList<>();

        for(String seatId: seatIds) {
            Seat seat = findSeatBySeatId(show, seatId);
            seats.add(seat);
        }

        return bookingManager.bookTickets(seats, show, user);
    }


    // 1. Add Movie
    public void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    // 2. Add Theater
    public void addTheater(Theater theater) {
        theaters.put(theater.getId(), theater);
    }

    // 3. Add Screen
    public void addScreen(String theaterId, Screen screen) {
        theaters.get(theaterId).addScreen(screen);
    }

    public void addShow(Show show) {
        shows.put(show.getId(), show);
    }

    // 4. Add City
    public void addCity(City city) {
        cities.put(city.getId(), city);
    }

    // 5. Add User
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }

}
