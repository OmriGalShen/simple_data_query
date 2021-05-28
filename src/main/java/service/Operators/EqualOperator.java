package service.Operators;

import api.CompareOperator;
import service.Database.Database;

import java.util.HashSet;
import java.util.stream.Collectors;

public class EqualOperator<T> extends CompareOperator<T> {
    public EqualOperator(CompareProperty property, T value) {
        super(Command.EQUAL, property, value);
    }

    @Override
    public HashSet<String> performOperator(HashSet<String> keys){
        Database db = Database.getInstance();
        switch (property){
            case id:
                return (keys.stream().filter(key->db.getItem(key).getId().equals(this.value))).collect(Collectors.toCollection(HashSet::new));
            case content:
                return (keys.stream().filter(key->db.getItem(key).getContent().equals(this.value))).collect(Collectors.toCollection(HashSet::new));
            case title:
                return (keys.stream().filter(key->db.getItem(key).getTitle().equals(this.value))).collect(Collectors.toCollection(HashSet::new));
            case views:
                return (keys.stream().filter(key->db.getItem(key).getViews()==((Integer)this.value))).collect(Collectors.toCollection(HashSet::new));
            case timestamp:
                return (keys.stream().filter(key->db.getItem(key).getTimestamp()==((Integer)this.value))).collect(Collectors.toCollection(HashSet::new));
        }

        return new HashSet<String>();
    }
}
