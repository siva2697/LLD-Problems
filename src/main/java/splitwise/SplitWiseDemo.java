package splitwise;

import splitwise.entities.Group;
import splitwise.entities.User;
import splitwise.entities.expense.Expense;
import splitwise.service.SplitWiseService;
import splitwise.strategy.EqualSplitStrategy;

import java.util.Arrays;
import java.util.UUID;

public class SplitWiseDemo {

    // 1. Create users, groups, expenses
    // 2. Get BalanceSheet
    // 3. Simplification of Balances
    public static void main(String[] args) {

        SplitWiseService service = SplitWiseService.getInstance();

        // 1.1 Create Users
        User user1 = new User("1", "Sai Kumar");
        User user2 = new User("2", "Sai Thanuj");
        User user3 = new User("3", "Dheeraj");

        // 1.2 Create Groups
        Group group1 = new Group("1", "Ahobilam", "Pilgrimage");
        service.addGroup(group1);

        service.addUser(user1, group1.getId());
        service.addUser(user2, group1.getId());
        service.addUser(user3, group1.getId());

        // 1.3 Create Expenses

        Expense expense1 = new Expense.ExpenseBuilder()
                .setId(UUID.randomUUID().toString())
                .setAmount(300)
                .setDescription("Breakfast")
                .setPaidBy(user1.getId())
                .setSplitStrategy(new EqualSplitStrategy())
                .setSplitValues(null)
                .setParticipants(Arrays.asList(user1.getId(), user2.getId(), user3.getId()))
                .build();

        Expense expense2 = new Expense.ExpenseBuilder()
                .setId(UUID.randomUUID().toString())
                .setAmount(300)
                .setDescription("Lunch")
                .setPaidBy(user2.getId())
                .setSplitStrategy(new EqualSplitStrategy())
                .setSplitValues(null)
                .setParticipants(Arrays.asList(user1.getId(), user2.getId(), user3.getId()))
                .build();


        service.addExpense(expense1, group1.getId());

        System.out.println(service.getBalances(user1.getId()).getSettlements());
        System.out.println(service.getBalances(user2.getId()).getSettlements());
        System.out.println(service.getBalances(user3.getId()).getSettlements());


        System.out.println(":::::::::::::::::::::::");

        service.addExpense(expense2, group1.getId());

        System.out.println(service.getBalances(user1.getId()).getSettlements());
        System.out.println(service.getBalances(user2.getId()).getSettlements());
        System.out.println(service.getBalances(user3.getId()).getSettlements());

        System.out.println(":::::::::::::::::::::::");


        Group group2 = new Group("2", "Varanasi", "Pilgrimage");
        service.addGroup(group2);

        service.addUser(user1, group2.getId());
        service.addUser(user2, group2.getId());
        service.addUser(user3, group2.getId());

        Expense expense3 = new Expense.ExpenseBuilder()
                .setId(UUID.randomUUID().toString())
                .setAmount(300)
                .setDescription("Lunch")
                .setPaidBy(user3.getId())
                .setSplitStrategy(new EqualSplitStrategy())
                .setSplitValues(null)
                .setParticipants(Arrays.asList(user1.getId(), user2.getId(), user3.getId()))
                .build();

        service.addExpense(expense3, group2.getId());

        System.out.println(service.getBalances(user1.getId()).getSettlements());
        System.out.println(service.getBalances(user2.getId()).getSettlements());
        System.out.println(service.getBalances(user3.getId()).getSettlements());

    }


}
