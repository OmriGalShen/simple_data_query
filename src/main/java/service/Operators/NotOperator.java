package service.Operators;

import api.BooleanOperator;
import api.Operator;
import service.Database.Database;
import service.Database.DBUtility;

import java.util.HashSet;

public class NotOperator extends BooleanOperator {
    private final Operator operator;
    public NotOperator(Operator operator) {
        super(Command.NOT);
        this.operator = operator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        return DBUtility.difference(Database.getInstance().getAllKeysSet(), operator.performOperator(keys));
    }
}
