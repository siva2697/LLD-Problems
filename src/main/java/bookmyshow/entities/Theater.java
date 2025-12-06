package bookmyshow.entities;

import java.util.ArrayList;
import java.util.List;

public class Theater {

    String id;
    String name;
    List<Screen> screens;

    public Theater(String id, String name) {
        this.id = id;
        this.name = name;
        screens = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }


}
