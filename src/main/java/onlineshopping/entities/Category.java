package onlineshopping.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {

    String id;
    String name;
    List<Item> items;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
