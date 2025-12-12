package splitwise.entities.expense;

import splitwise.entities.Split;
import splitwise.strategy.ISplitStrategy;

import java.time.Instant;
import java.util.List;

public class Expense {

    private final String id;
    private final String description;
    private final String paidBy;
    private final List<Split> splits;
    private final double amount;
    private final Instant timestamp;

    private Expense(ExpenseBuilder builder) {
        this.id = builder.id;
        this.paidBy = builder.paidBy;
        this.amount = builder.amount;
        this.description = builder.description;
        this.timestamp = builder.timestamp;

        this.splits = builder.splitStrategy.calculateSplits(
                builder.paidBy, builder.amount, builder.participants, builder.splitValues
        );
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public String getDescription() {
        return description;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public static class ExpenseBuilder {
        private String id;
        private String paidBy;
        private String description;
        private double amount;
        private Instant timestamp;

        private List<String> participants;
        private List<Double> splitValues;
        private ISplitStrategy splitStrategy;

        public ExpenseBuilder setId(String id) {
            this.id = id;
            return this;
        };

        public ExpenseBuilder setPaidBy(String paidBy) {
            this.paidBy = paidBy;
            return this;
        }

        public ExpenseBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseBuilder setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ExpenseBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ExpenseBuilder setParticipants(List<String> participants) {
            this.participants = participants;
            return this;
        }

        public ExpenseBuilder setSplitValues(List<Double> splitValues) {
            this.splitValues = splitValues;
            return this;
        }

        public ExpenseBuilder setSplitStrategy(ISplitStrategy splitStrategy) {
            this.splitStrategy = splitStrategy;
            return this;
        }

        public Expense build() {
            if(splitStrategy == null) {
                throw new IllegalStateException("Split strategy is required");
            }
            return new Expense(this);
        }
    }
}
