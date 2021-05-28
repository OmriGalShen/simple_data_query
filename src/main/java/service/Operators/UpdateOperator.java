package service.Operators;

import api.CompareOperator;
import api.Item;
import api.ModifyOperator;
import service.Database.Database;

import java.util.HashSet;

public class UpdateOperator<T> extends ModifyOperator {
    private final CompareOperator.CompareProperty property;
    private final T value;

    public UpdateOperator(Command cmd, String id, CompareOperator.CompareProperty property, T value) {
        super(cmd, id);
        this.property = property;
        this.value = value;
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        Item item = Database.getInstance().getItem(id);
        if(item!=null){
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
        }}
        HashSet<String> hs = new HashSet<>();
        hs.add(id);
        return hs;
    }
}
