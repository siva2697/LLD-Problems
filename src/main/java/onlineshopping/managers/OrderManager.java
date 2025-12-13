package onlineshopping.managers;

import onlineshopping.entities.Cart;
import onlineshopping.entities.Order;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrderManager {

    Map<String, Order> orders;
    InventoryManager inventoryManager;

    public OrderManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        orders = new ConcurrentHashMap<>();
    }

    public Order placeOrder(Cart cart) {

        boolean didReserveCartItems = inventoryManager.reserveItems(cart);

        if(!didReserveCartItems) {
            System.out.println("Items unavailable");
            return null;
        }

        boolean didPaymentSuccess = true;
//        boolean didPaymentSuccess = Math.random() > 0.2;

        if(!didPaymentSuccess) {
            inventoryManager.releaseInventory(cart.getId());
            return null;
        }

        inventoryManager.confirmOrder(cart.getId());

        // Order Construction
        Order order = new Order.OrderBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .setCartItems(cart.getCartItems())
                .setTimestamp(Instant.now())
                .build();

        orders.put(order.getId(), order);

        return order;
    }

}
