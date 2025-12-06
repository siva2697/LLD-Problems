package bookmyshow.entities;

import java.util.ArrayList;
import java.util.List;

public class City {

    String id;
    String name;
    List<Theater> theaters;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
        this.theaters = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public List<Theater> getTheaters() {
        return theaters;
    }
}
