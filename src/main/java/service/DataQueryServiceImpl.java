package service;

import api.DataQueryService;
import api.Item;
import api.Operator;
import service.Database.Database;
import service.Parser.QueryParser;

import java.util.List;

public class DataQueryServiceImpl implements DataQueryService {



    public List<Item> query(String query) throws Exception {
        Operator q = QueryParser.stringToQuery(query);
        return Database.getInstance().performQuery(q);
    }

    public void save(Item Item) throws Exception {
        Database.getInstance().insert(Item);
    }
}
