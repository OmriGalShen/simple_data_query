package service.Operators;

import api.Operator;
import service.Database.Database;

import java.util.HashSet;

public class EmptyOperator extends Operator {
    public EmptyOperator() {
        super(Command.EQUAL);
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        return Database.getInstance().getAllKeysSet();
    }
}
