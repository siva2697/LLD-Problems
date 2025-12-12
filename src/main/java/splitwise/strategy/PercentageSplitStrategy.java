package splitwise.strategy;

import splitwise.entities.Split;

import java.util.ArrayList;
import java.util.List;

public class PercentageSplitStrategy implements ISplitStrategy {

    private boolean areSplitValuesValid(List<Double> splitValues) {

        double calculatedTotalPercentage = 0;

        for(Double value: splitValues) {
            calculatedTotalPercentage += value;
        }

        return calculatedTotalPercentage == 100.0;
    }

    @Override
    public List<Split> calculateSplits(String paidBy, double amount, List<String> participants, List<Double> splitValues) {

        if(participants.size() != splitValues.size()) {
            throw new IllegalArgumentException("Participants and splitValues size dont match");
        }

        if(!areSplitValuesValid(splitValues)) {
            throw new IllegalArgumentException("Sum of percentages must be equal to 100");
        }

        List<Split> splits = new ArrayList<>();

        for(int i=0; i<participants.size(); i++) {

            double currAmount = (amount * splitValues.get(i))/100.0;

            splits.add(new Split(participants.get(i), currAmount));
        }

        return splits;
    }
}
