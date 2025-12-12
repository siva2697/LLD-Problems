package splitwise.strategy;

import splitwise.entities.Split;

import java.util.ArrayList;
import java.util.List;

public class ExactSplitStrategy implements ISplitStrategy {


    private boolean doesTotalAmountMatch(double expectedAmount, List<Double> splitValues) {

        double calculatedAmount = 0;
        for(Double value: splitValues) {
            calculatedAmount += value;
        }

        if(calculatedAmount != expectedAmount) {
            return false;
        }

        return true;
    }

    @Override
    public List<Split> calculateSplits(String paidBy, double amount, List<String> participants, List<Double> splitValues) {

        if(participants.size() != splitValues.size()) {
            throw new IllegalArgumentException("Number of participants and split values dont match");
        }

        if(!doesTotalAmountMatch(amount, splitValues)) {
            throw new IllegalArgumentException("Totals dont match");
        }

        List<Split> splits = new ArrayList<>();
        for(int i=0; i<participants.size(); i++) {
            splits.add(new Split(participants.get(i), splitValues.get(i)));
        }

        return splits;
    }
}
