package onlineshopping.entities;

import java.time.Instant;
import java.util.List;

public class Order {

    String id;
    List<CartItem> cartItems;
    Instant timestamp;
    // TODO: PaymentDetails

    public String getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    private Order(OrderBuilder orderBuilder) {
        this.id = orderBuilder.orderId;
        this.cartItems = orderBuilder.cartItems;
        this.timestamp = orderBuilder.timestamp;
    }

    public static class OrderBuilder {

        String orderId;
        List<CartItem> cartItems;
        Instant timestamp;

        public OrderBuilder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder setCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public OrderBuilder setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Order build() {
            // Add Validations
            return new Order(this);
        }

    }



}
