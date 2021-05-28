package service.Database;

import api.Item;
import api.NoDataFound;
import api.Operator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private static Database instance = null; // Singleton pattern
    private final HashMap<String,Item> db;

    private Database() {
        this.db = new HashMap<>();
    }

    public static Database getInstance() // Singleton pattern
    {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    public Item getItem(String key){return db.get(key);}

    public boolean contains(String id) {return db.containsKey(id);}

    public void insert(Item item) throws Exception {
        db.put(item.getId(),(Item) item.clone()); // If id already exist override Item
    }

    public HashSet<String> getAllKeysSet() {return new HashSet<>(db.keySet());}

    public List<Item> performQuery(Operator q) throws NoDataFound {
        HashSet<String> idSet = new HashSet<>(db.keySet());
        HashSet<String> keySet = q.performOperator(idSet);
        return getItemsFromKeys(keySet);
    }

    public List<Item> getItemsFromKeys (HashSet<String> keySet){return keySet.stream().map(db::get).collect(Collectors.toList());}
}
