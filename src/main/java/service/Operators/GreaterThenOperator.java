package service.Operators;

import api.CompareOperator;
import service.Database.Database;

import java.util.HashSet;
import java.util.stream.Collectors;

public class GreaterThenOperator extends CompareOperator<Integer> {
    public GreaterThenOperator(CompareProperty property, Integer value) {
        super(Command.GREATER_THAN, property, value);
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        Database db = Database.getInstance();
        switch (property){
            case views:
                return (keys.stream().filter(key->db.getItem(key).getViews()>this.value)).collect(Collectors.toCollection(HashSet::new));
            case timestamp:
                return (keys.stream().filter(key->db.getItem(key).getTimestamp()>this.value)).collect(Collectors.toCollection(HashSet::new));
        }

        return new HashSet<String>();
    }
}
