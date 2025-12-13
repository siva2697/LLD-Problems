package onlineshopping.managers;

import onlineshopping.entities.Cart;
import onlineshopping.entities.CartItem;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InventoryManager {

    private final int TIME_THRESHOLD = 15;

    // Locks for thread safe access to item stocks
    Map<String, Object> itemLocks;

    // Locks for thread safe access to cart
    Map<String, Object> cartLocks;

    // Inventory Details (itemId -> Units)
    Map<String, Integer> inventory;

    // Reservations (cartId -> (itemId -> units))
    Map<String, Map<String, Integer>> reserved;

    // Scheduler to run the release inventory
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public InventoryManager() {
        itemLocks = new ConcurrentHashMap<>();
        cartLocks = new ConcurrentHashMap<>();
        inventory = new ConcurrentHashMap<>();
        reserved = new ConcurrentHashMap<>();
    }

    public boolean reserveItems(Cart cart) {

        Object lock = cartLocks.computeIfAbsent(cart.getId(), k -> new Object());

        Map<String, Integer> successfulReservation = new ConcurrentHashMap<>();

        // 1. Need clarity here on concurrency issue areas
        synchronized (lock) {

            Collections.sort(cart.getCartItems(), (a, b) -> a.getItemId().compareTo(b.getItemId()));

            try {

                for (CartItem cartItem : cart.getCartItems()) {

                    String itemId = cartItem.getItemId();
                    Object itemLock = itemLocks.computeIfAbsent(itemId, k -> new Object());

                    synchronized (itemLock) {

                        int availableUnits = inventory.getOrDefault(itemId, 0);
                        int requiredUnits = cartItem.getUnits();

                        if (availableUnits >= requiredUnits) {
                            successfulReservation.put(itemId, requiredUnits);
                            inventory.put(itemId, availableUnits - requiredUnits);
                        } else {
                            System.out.println("Item :" + itemId + " not available");
                            throw new IllegalStateException("Insufficient inventory. Performing rollback");
                        }
                    }
                }

                reserved.put(cart.getId(), successfulReservation);

            } catch (IllegalStateException ex) {
                performRollback(successfulReservation);
                return false;
            }
        }

        scheduledExecutorService.schedule(
                () -> releaseInventory(cart.getId()),
                TIME_THRESHOLD,
                TimeUnit.MINUTES
        );

        return true;
    }

    public void performRollback(Map<String, Integer> reservation) {

        reservation.forEach((itemId, reservedUnits) -> {

            Object itemLock = itemLocks.computeIfAbsent(itemId, k -> new Object());

            synchronized (itemLock) {
                int currentAvailable = inventory.getOrDefault(itemId, 0);
                inventory.put(itemId, currentAvailable + reservation.get(itemId));
            }
        });

    }

    public void confirmOrder(String cartId) {
        reserved.remove(cartId);
    }

    public void releaseInventory(String cartId) {

        Object lock = cartLocks.computeIfAbsent(cartId, k -> new Object());

        synchronized (lock) {

            Map<String, Integer> reservation = reserved.get(cartId);

            if (reservation == null) {
                return;
            }

            reservation.forEach((itemId, reservedUnits) -> {

                Object itemLock = itemLocks.computeIfAbsent(itemId, k -> new Object());

                synchronized (itemLock) {
                    int currentAvailable = inventory.getOrDefault(itemId, 0);
                    inventory.put(itemId, currentAvailable + reservedUnits);
                }

            });
        }

        reserved.remove(cartId);
    }

    public void updateInventory(String itemId, int units) {

        Object lock = itemLocks.computeIfAbsent(itemId, k -> new Object());
        synchronized (lock) {
            inventory.put(itemId, units);
        }
    }

    public int getInventory(String itemId) {
        return inventory.getOrDefault(itemId, 0);
    }

}































//
//
//    // 1. Time out Configuration
//    int INVENTORY_TIME_OUT = 15; // minutes
//
//    // 2. Available stock for each item
//    Map<String, Integer> inventory;
//
//    // Locks for ensuring thread safe access to individual item stock counts
//    Map<String, Object> itemLocks;
//
//    // Tracking reserved stock (UserId -> (ItemId -> Units reserved))
//    Map<String, Map<String, Integer>> reserved;
//
//    // Locks for thread safe access to cart's reservation process
//    Map<String, Object> cartLocks;
//
//    // Timeout management to release the reserved inventory
//    ScheduledExecutorService scheduledExecutorService;
//
//    public InventoryManager() {
//        this.itemLocks = new ConcurrentHashMap<>();
//        this.reserved = new ConcurrentHashMap<>();
//        this.inventory = new ConcurrentHashMap<>();
//        this.cartLocks = new ConcurrentHashMap<>();
//        this.scheduledExecutorService= new ScheduledThreadPoolExecutor(1);
//    }
//
//    public boolean reserveInventory(Cart cart, String userId) {
//
//        Object cartLock = cartLocks.computeIfAbsent(cart.getId(), k -> new Object());
//
//        synchronized (cartLock) {
//
//            List<CartItem> cartItems = cart.getCartItems();
//
//            // To avoid the deadlocks
//            cartItems.sort((a, b) -> a.getItemId().compareTo(b.getItemId()));
//
//            Map<String, Integer> successfulReservation = new ConcurrentHashMap<>();
//
//            try {
//
//                for (CartItem cartItem : cartItems) {
//
//                    String itemId = cartItem.getItemId();
//
//                    Object itemLock = itemLocks.computeIfAbsent(itemId, k -> new Object());
//
//                    synchronized (itemLock) {
//
//                        int unitsAvailable = inventory.getOrDefault(itemId, 0);
//                        int requested = cartItem.getUnits();
//
//                        if (unitsAvailable >= requested) {
//                            successfulReservation.put(itemId, requested);
//                            inventory.put(itemId, unitsAvailable - requested);
//                        } else {
//                            System.out.println("Item :" + itemId + " not available");
//                            throw new IllegalStateException("Insufficient inventory. Performing rollback");
//                        }
//                    }
//                }
//
//                reserved.put(userId, successfulReservation);
//
//            } catch (IllegalStateException ex) {
//                performRollback(successfulReservation);
//                return false;
//            }
//        }
//
//        scheduledExecutorService.schedule(
//                () -> releaseInventory(cart.getId(), userId),
//                INVENTORY_TIME_OUT,
//                TimeUnit.MINUTES
//        );
//
//        return true;
//    }
//
//    private void performRollback(Map<String, Integer> reservation) {
//
//        reservation.forEach((itemId, reservedUnits) -> {
//
//            Object lock = itemLocks.get(itemId);
//
//            if(lock!=null) {
//                synchronized (lock) {
//                    int currentAvailable = inventory.getOrDefault(itemId, 0);
//                    inventory.put(itemId, currentAvailable + reservedUnits);
//                    System.out.println("Released the reserved inventory for the item: " + itemId);
//                }
//            }
//        });
//    }
//
//    public void confirmBooking(String userId) {
//        reserved.remove(userId);
//    }
//
//    // Remove from reservations map about the user, update the inventory back
//    private void releaseInventory(String cartId, String userId) {
//
//        Object cartLock = cartLocks.get(cartId);
//
//        synchronized (cartLock) {
//
//            Map<String, Integer> userReservation = reserved.get(userId);
//            reserved.remove(userId);
//
//            cartLocks.remove(cartId);
//
//            if(userReservation == null || userReservation.isEmpty()) {
//                System.out.println("Nothing to release back");
//                return;
//            }
//
//            performRollback(userReservation);
//        }
//    }
//
//

