package splitwise.entities;

public class Split {
    String userId;
    double amount;

    public Split(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }
}
