package service;

import api.DataQueryService;
import api.Item;
import api.NoDataFound;
import api.Operator;
import service.Database.Database;
import service.Parser.QueryParser;

import java.util.List;

public class DataQueryServiceImpl implements DataQueryService {



    public List<Item> query(String query) throws Exception {
        Operator q = QueryParser.stringToQuery(query);
        List<Item> items = Database.getInstance().performQuery(q);
        if(items.size()==0)
            throw new NoDataFound(q.getCmd().name());
        return items;

    }

    public void save(Item Item) throws Exception {
        Database.getInstance().insert(Item);
    }
}
