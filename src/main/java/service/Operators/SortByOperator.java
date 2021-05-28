package service.Operators;

import api.BooleanOperator;
import api.CompareOperator;
import api.Item;
import api.Operator;
import service.Database.DBUtility;
import service.Database.Database;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class SortByOperator extends CompareOperator<Boolean>{

    private final Operator operator;

    public SortByOperator(Operator operator, CompareProperty property, boolean isASC) {
        super(Command.SORT_BY, property, isASC);
        this.operator = operator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys) {
        Database db = Database.getInstance();
        switch (property){
            case views:
                return operator.performOperator(keys).stream().map(db::getItem).sorted(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        int res = Integer.compare(o1.getViews(),o2.getViews());
                        return value? res:-1*res;
                    }
                }).map(Item::getId).collect(Collectors.toCollection(HashSet::new));
            case timestamp:
                return operator.performOperator(keys).stream().map(db::getItem).sorted(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return value? Integer.compare(o1.getTimestamp(),o2.getTimestamp()):Integer.compare(o2.getTimestamp(),o1.getTimestamp());
                    }
                }).map(Item::getId).collect(Collectors.toCollection(HashSet::new));
        }

        return new HashSet<String>();
    }

}
