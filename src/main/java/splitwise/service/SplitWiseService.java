package splitwise.service;

import splitwise.entities.BalanceSheet;
import splitwise.entities.Group;
import splitwise.entities.User;
import splitwise.entities.expense.Expense;
import splitwise.managers.BalanceSheetManagerCoarseGrainedAccess;
import splitwise.managers.IBalanceSheetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SplitWiseService {

    private Map<String, User> users;
    private Map<String, Group> groups;
    private IBalanceSheetManager balanceSheetManager;
    private Map<String, List<String>> userGroups;

    private static volatile SplitWiseService instance;

    private SplitWiseService() {
        this.balanceSheetManager = new BalanceSheetManagerCoarseGrainedAccess();
        this.groups = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.userGroups = new ConcurrentHashMap<>();
    }

    public static SplitWiseService getInstance() {

        if(instance == null) {
            synchronized (SplitWiseService.class) {
                if(instance == null) {
                    instance = new SplitWiseService();
                }
            }
        }

        return instance;
    }

    public void addUser(User user, String groupId) {
        this.users.put(user.getId(), user);
        userGroups.computeIfAbsent(user.getId(), k-> new ArrayList<>()).add(groupId);
        Group group = this.groups.get(groupId);
        group.addParticipant(user.getId());
    }

    public void addGroup(Group group) {
        this.groups.put(group.getId(), group);
    }

    public void addExpense(Expense expense, String groupId) {
        balanceSheetManager.addExpense(expense, groupId);
    }

    public BalanceSheet getBalances(String userId) {

        List<String> groupIds = userGroups.get(userId);

        return balanceSheetManager.getBalances(userId, groupIds);
    }


}
