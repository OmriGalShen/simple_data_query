package service.Operators;

import api.ModifyOperator;
import service.Database.Database;

import java.util.HashSet;

public class DeleteOperator extends ModifyOperator {
    public DeleteOperator(Command cmd, String id) {
        super(cmd, id);
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        Database db = Database.getInstance();
        db.removeItem(id);
        return keys;
    }
}
