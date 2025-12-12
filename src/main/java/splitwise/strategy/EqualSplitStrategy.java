package splitwise.strategy;

import splitwise.entities.Split;

import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> calculateSplits(String paidBy, double amount, List<String> participants, List<Double> splitValues) {

        List<Split> splits = new ArrayList<>();
        double amountPerPerson = amount/participants.size();

        for (String participant : participants) {
            splits.add(new Split(participant, amountPerPerson));
        }

        return splits;
    }
}
