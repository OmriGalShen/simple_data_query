package service.Operators;

import api.BooleanOperator;
import api.CompareOperator;
import api.ModifyOperator;
import api.Operator;
import service.Database.Database;

import java.util.HashSet;

public class UpdateOperator<T> extends ModifyOperator {
    private final Operator operator;
    protected final CompareOperator.CompareProperty property;
    protected final T value;

    public UpdateOperator(Operator operator, CompareOperator.CompareProperty property, T value) {
        super(Command.UPDATE);
        this.operator = operator;
        this.property = property;
        this.value = value;
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
