package service.Operators;

import api.BooleanOperator;
import api.Operator;

import java.util.HashSet;

public class AllOperator extends BooleanOperator {
    public AllOperator() {
        super(Command.ALL);
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        return keys;
    }
}
