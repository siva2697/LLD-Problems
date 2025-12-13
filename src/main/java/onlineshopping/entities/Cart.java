package onlineshopping.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    String id;
    String userId; // Owner of the cart
    List<CartItem> cartItems;

    public Cart(String id, String userId) {
        this.id = id;
        this.userId = userId;
        cartItems = new ArrayList<>();
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getId() {
        return id;
    }
}
