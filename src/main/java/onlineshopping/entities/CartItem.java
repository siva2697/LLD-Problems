package onlineshopping.entities;

public class CartItem {
    String itemId;
    int units;

    public CartItem(String itemId, int units) {
        this.itemId = itemId;
        this.units = units;
    }

    public String getItemId() {
        return itemId;
    }

    public int getUnits() {
        return units;
    }
}
