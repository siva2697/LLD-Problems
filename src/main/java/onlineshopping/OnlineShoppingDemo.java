package onlineshopping;

import onlineshopping.entities.Category;
import onlineshopping.entities.Item;
import onlineshopping.entities.User;
import onlineshopping.service.OnlineShoppingService;

import java.util.Arrays;

/*
    1. Inventory Management -> Create Items, Categories
    2. Cart Management -> Add Items to cart
    3. User Management -> Create Users
    4. Demonstration -> Order creation (Must), Concurrency (Good to have)
*/

public class OnlineShoppingDemo {

    public static void main(String args[]) throws InterruptedException {


        Item item1 = new Item("1", "iphone", "mobile");
        Item item2 = new Item("2", "samsung", "mobile");
        Category category1 = new Category("mobile", "Mobile Phone");

        User user1 = new User("1", "Sai");
        User user2 = new User("2", "Thanuj");
        User user3 = new User("3", "Dheeraj");
        User user4 = new User("3", "Gowtham");

        OnlineShoppingService service = OnlineShoppingService.getInstance();

        service.addCategory(category1);
        service.addItem(item1, category1.getId());
        service.addItem(item2, category1.getId());
        service.updateInventory(item1.getId(), 20);
        service.updateInventory(item2.getId(), 10);

        System.out.println(":::::::1. Search Item ::::::");

        System.out.println(service.searchByItem(item1.getId()));

        System.out.println(":::::::2. Search by Category::::");


        System.out.println(service.searchByCategory(category1.getId()));

        System.out.println("::::3. Add Items to a Cart ::::::::::");

        String cartId1 = service.addItemsToCart(Arrays.asList(item1.getId(), item2.getId()), Arrays.asList(20, 10), null, user1.getUserId());

        System.out.println("Order 1 -> " + service.placeOrder(cartId1).getId());

        Thread.sleep(5000);

        System.out.println(":::: 4. Concurrency ::::::::::");

        String cartId2 = service.addItemsToCart(Arrays.asList(item2.getId()), Arrays.asList(10), null, user2.getUserId());
        String cartId3 = service.addItemsToCart(Arrays.asList(item2.getId()), Arrays.asList(10), null, user3.getUserId());

        Thread t1 = new Thread(() -> {
            System.out.println("Order 2 -> " + service.placeOrder(cartId2).getId());
        });

        Thread t2 = new Thread(() -> {
            System.out.println("Order 3 -> " + service.placeOrder(cartId3).getId());
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        String cartId4 = service.addItemsToCart(Arrays.asList(item2.getId()), Arrays.asList(10), null, user4.getUserId());


    }
}
