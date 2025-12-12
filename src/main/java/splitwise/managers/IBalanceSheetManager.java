package splitwise.managers;

import splitwise.entities.BalanceSheet;
import splitwise.entities.expense.Expense;

import java.util.List;

public interface IBalanceSheetManager {

    public void addExpense(Expense expense, String groupId);

    public BalanceSheet getBalances(String userId, List<String> groupIds);

    public void deleteExpense(String expenseId);
}
