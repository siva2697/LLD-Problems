package onlineshopping.entities;

public class Item {

    String id;
    String name;
    String categoryId;

    public Item(String id, String name, String categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
