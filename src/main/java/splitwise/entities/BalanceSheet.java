package splitwise.entities;

import java.util.Map;

public class BalanceSheet {

    private String userId;
    private Map<String, Double> settlements;

    public BalanceSheet(String userId) {
        this.userId = userId;
    }

    public void setSettlements(Map<String, Double> settlements) {
        this.settlements = settlements;
    }

    public Map<String, Double> getSettlements() {
        return settlements;
    }
}
