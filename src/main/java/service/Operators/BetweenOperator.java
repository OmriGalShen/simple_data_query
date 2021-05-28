package service.Operators;

import api.CompareOperator;
import service.Database.Database;

import java.util.HashSet;
import java.util.stream.Collectors;

public class BetweenOperator extends CompareOperator<Integer>{
    private final Integer compareValue;

    public BetweenOperator( CompareProperty property, Integer value, Integer compareValue) {
        super(Command.BETWEEN, property, value);
        this.compareValue = compareValue;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        Database db = Database.getInstance();
        switch (property){
            case views:
                return (keys.stream().filter(key-> checkPredicate(key,db)).collect(Collectors.toCollection(HashSet::new)));
            case timestamp:
                return (keys.stream().filter(key->db.getItem(key).getTimestamp()>this.value)).collect(Collectors.toCollection(HashSet::new));
        }

        return new HashSet<String>();
    }

    private boolean checkPredicate(String key, Database db){
        int views = db.getItem(key).getViews();
        return ((views >= this.value) && (views <= this.compareValue));
    }
}
