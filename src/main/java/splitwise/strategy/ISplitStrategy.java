package splitwise.strategy;

import splitwise.entities.Split;

import java.util.List;

public interface ISplitStrategy {

    public List<Split> calculateSplits(String paidBy, double amount, List<String> participants, List<Double> splitValues);

}
