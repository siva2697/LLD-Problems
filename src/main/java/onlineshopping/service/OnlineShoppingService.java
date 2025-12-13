package onlineshopping.service;

import onlineshopping.entities.*;
import onlineshopping.managers.CartManager;
import onlineshopping.managers.InventoryManager;
import onlineshopping.managers.OrderManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineShoppingService {

    InventoryManager inventoryManager;
    CartManager cartManager;
    OrderManager orderManager;

    Map<String, Item> items;
    Map<String, Category> categories;

    // 1. singleton instance of OnlineShoppingService
    private static volatile OnlineShoppingService instance;

    private OnlineShoppingService() {
        inventoryManager = new InventoryManager();
        orderManager = new OrderManager(inventoryManager);
        cartManager = new CartManager();

        items = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
    }

    // Using DDL for creating the singleton instance
    public static OnlineShoppingService getInstance() {
        if(instance == null) {
            synchronized (OnlineShoppingService.class) {
                instance = new OnlineShoppingService();
            }
        }
        return instance;
    }

    public Item searchByItem(String itemId) {
        return items.get(itemId);
    }

    public List<Item> searchByCategory(String categoryId) {
        Category category = categories.get(categoryId);
        return category.getItems();
    }

    public void addItem(Item item, String categoryId) {
        items.put(item.getId(), item);
        Category category = categories.get(categoryId);
        category.addItem(item);
    }

    public void addCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public void updateInventory(String itemId, int units) {
        inventoryManager.updateInventory(itemId, units);
    }

    public String addItemsToCart(List<String> itemIds, List<Integer> quantities, String cartId, String userId) {
        if(cartId == null) {
            cartId = UUID.randomUUID().toString();
        }
        cartManager.addItems(cartId, userId, itemIds, quantities);
        return cartId;
    }

    public Order placeOrder(String cartId) {

        Cart cart = cartManager.getCart(cartId);

        return orderManager.placeOrder(cart);
    }

    public int getInventory(String itemId) {
        return inventoryManager.getInventory(itemId);
    }

}
