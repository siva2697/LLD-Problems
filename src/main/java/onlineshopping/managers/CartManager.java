package onlineshopping.managers;

import onlineshopping.entities.Cart;
import onlineshopping.entities.CartItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CartManager {

    // cartId -> Cart
    Map<String, Cart> carts;

    public CartManager() {
        carts = new ConcurrentHashMap<>();
    }

    public String addItems(String cartId, String userId, List<String> itemIds, List<Integer> units) {

        carts.computeIfAbsent(cartId, k-> new Cart(cartId, userId));

        Cart cart = carts.get(cartId);
        for(int i=0; i< itemIds.size(); i++) {
            cart.addCartItem(new CartItem(itemIds.get(i), units.get(i)));
        }

        return cartId;
    }

    public Cart getCart(String cartId) {
        return carts.get(cartId);
    }
}
