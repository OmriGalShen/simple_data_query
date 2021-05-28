package service.Operators;

import api.CompareOperator;
import api.Operator;
import service.Database.Database;

import java.util.HashSet;

public class UpdateOperator<T> extends CompareOperator<T> {
    private final Operator operator;

    public UpdateOperator(CompareProperty property, T value, Operator operator) {
        super(Command.UPDATE, property, value);
        this.operator = operator;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        HashSet<String> ketSet = operator.performOperator(keys);
        Database.getInstance().getItemsFromKeys(ketSet).forEach(item -> {
            switch (property){
                case id:
                    item.setId(value.toString()); break;
                case content:
                    item.setContent(value.toString());break;
                case title:
                    item.setTitle(value.toString());break;
                case views:
                    item.setViews((Integer) value);break;
                case timestamp:
                    item.setTimestamp((Integer) value);break;
            }});
        return ketSet;

    }
}
