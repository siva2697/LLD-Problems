package splitwise.managers;

import splitwise.entities.BalanceSheet;
import splitwise.entities.Split;
import splitwise.entities.expense.Expense;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheetManagerCoarseGrainedAccess implements IBalanceSheetManager {

    // Locks for thread safe access to groups
    private final Map<String, Object> groupLocks;

    // Manage the balance sheet of group and their users
    private final Map<String, Map<String, Map<String, Double>>> balanceSheet;

    public BalanceSheetManagerCoarseGrainedAccess() {
        groupLocks = new ConcurrentHashMap<>();
        balanceSheet = new ConcurrentHashMap<>();
    }

    @Override
    public void addExpense(Expense expense, String groupId) {

        Object lock = groupLocks.computeIfAbsent(groupId, k -> new Object());

        synchronized (lock) {

            String creditorId = expense.getPaidBy();

            for (Split split : expense.getSplits()) {

                String debtorId = split.getUserId();
                double amount = split.getAmount();

                if (debtorId.equals(creditorId)) {
                    continue;
                }

                updateBalanceSheet(debtorId, creditorId, amount, groupId);
            }
        }
    }


    private void updateBalanceSheet(String debtorId, String creditorId, double amount, String groupId) {

        Map<String, Map<String, Double>> groupBalances = balanceSheet.computeIfAbsent(groupId, k-> new ConcurrentHashMap<>());
        Map<String, Double> debtorConnections = groupBalances.computeIfAbsent(debtorId, k-> new ConcurrentHashMap<>());
        debtorConnections.merge(creditorId, amount, Double::sum);

    }

    @Override
    public BalanceSheet getBalances(String userId, List<String> groupIds) {

        BalanceSheet balanceSheet1 = new BalanceSheet(userId);
        Map<String ,Double> settlements = new HashMap<>();

        for(String groupId: groupIds) {

            Object lock = groupLocks.computeIfAbsent(groupId, k -> new Object());

            synchronized (lock) {

                Map<String, Map<String, Double>> groupBalances = balanceSheet.get(groupId);

                if(groupBalances == null) {
                    continue;
                }

                // 1. Check if user owes money to someone
                if(groupBalances.containsKey(userId)) {
                    Map<String, Double> debts = groupBalances.get(userId);
                    debts.forEach((creditorId, amount) -> {
                        settlements.merge(creditorId, -amount, Double::sum);
                    });
                }

                // 2. Check if anyone owes money to user
                groupBalances.forEach((debtorId, debtorMap) -> {
                    if(!debtorId.equals(userId) && debtorMap.containsKey(userId)) {
                        double amount = debtorMap.get(userId);
                        settlements.merge(debtorId, amount, Double::sum);
                    }
                });

            }
        }

        balanceSheet1.setSettlements(settlements);
        return balanceSheet1;
    }


    // TODO: Can be explored later
    @Override
    public void deleteExpense(String expenseId) {}
}
