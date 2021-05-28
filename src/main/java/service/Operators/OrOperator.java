package service.Operators;

import api.BooleanOperator;
import api.Operator;
import service.Database.DBUtility;

import java.util.HashSet;

public class OrOperator extends BooleanOperator {
    private final Operator firstOperator, secondOperator;

    public OrOperator(Operator firstOperator, Operator secondOperator) {
        super(Command.OR);
        this.firstOperator = firstOperator;
        this.secondOperator = secondOperator;
    }

    public Operator getFirstQuery() {
        return firstOperator;
    }

    public Operator getSecondQuery() {
        return secondOperator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        return DBUtility.Union(firstOperator.performOperator(keys), secondOperator.performOperator(keys));
    }
}
