package service.Operators;

import api.CompareOperator;
import api.Item;
import api.Operator;
import service.Database.Database;

import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MaxOperator extends CompareOperator<Integer> {
    private final Operator operator;

    public MaxOperator(CompareProperty property,Operator operator) {
        super(Command.MAX, property, 0);
        this.operator=operator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        Database db = Database.getInstance();
        HashSet<String> ketSet = operator.performOperator(keys);
        switch (property){
            case views:
                return ketSet.stream().map(db::getItem).max(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return Integer.compare(o1.getViews(),o2.getViews());
                    }
                }).map(Item::getId).stream().collect(Collectors.toCollection(HashSet::new));
            case timestamp:
                return ketSet.stream().map(db::getItem).max(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return Integer.compare(o1.getTimestamp(),o2.getTimestamp());
                    }
                }).map(Item::getId).stream().collect(Collectors.toCollection(HashSet::new));
        }
        return new HashSet<String>();
    }
}
