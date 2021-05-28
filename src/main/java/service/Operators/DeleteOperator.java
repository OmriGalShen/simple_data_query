package service.Operators;

import api.BooleanOperator;
import api.Operator;
import service.Database.Database;

import java.util.HashSet;

public class DeleteOperator extends BooleanOperator {
    private final Operator operator;

    public DeleteOperator(Operator operator) {
        super(Command.DELETE);
        this.operator = operator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        Database db = Database.getInstance();
        HashSet<String> ketSet = operator.performOperator(keys);
        ketSet.forEach(db::removeItem);
        return new HashSet<>();
    }
}
