package service;

import api.DataQueryService;
import api.Item;
import api.NoDataFound;
import api.Operator;
import service.Database.Database;
import service.Parser.QueryParser;

import java.util.List;

/**
 * A simple service able to store and retrieve data.
 */
public class DataQueryServiceImpl implements DataQueryService {

    /**
     * Takes query as input and returns matching entries
     * @param query valid query string
     * @return matching entries
     * @throws Exception NoDataFound or QueryParseException with relevant info
     */
    public List<Item> query(String query) throws Exception {
        Operator q = QueryParser.stringToQuery(query);
        List<Item> items = Database.getInstance().performQuery(q);
        if(items.size()==0)
            throw new NoDataFound(q.getCmd().name());
        return items;

    }

    /**
     * Take entity and stores it. ID must remain unique.
     * If record with given ID already exists, it should be overwritten
     * @param Item entity to store
     * @throws Exception QueryParseException with relevant info
     */
    public void save(Item Item) throws Exception {
        Database.getInstance().insert(Item);
    }
}
