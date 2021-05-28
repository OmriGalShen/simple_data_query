package service;

import api.DataQueryService;
import api.Item;
import api.Operator;
import service.Database.Database;
import service.Parser.QueryParser;

import java.util.List;

public class DataQueryServiceImpl implements DataQueryService {

    public static void main(String[] args) {
        Database db = Database.getInstance();
        Item s1 = new Item("s1", "My First Post", "Hello World!", 1, 1555832341);
        Item s2 = new Item("s2", "My First Post", "Hello World!", 2, 1555832341);
        Item s3 = new Item("s3", "My First Post", "Hello World!", 15, 1555832341);
        Item s4 = new Item("s4", "My First Post", "Hello World!", 50, 1555832341);
        Item s5 = new Item("s5", "My First Post", "Hello World!", 100, 1555832341);
        Item s6 = new Item("s6", "My First Post", "Hello World!", 500, 1555832341);
        DataQueryService dataQueryService = new DataQueryServiceImpl();
        try{
            dataQueryService.save(s1);
            dataQueryService.save(s2);
            dataQueryService.save(s3);
            dataQueryService.save(s4);
            dataQueryService.save(s5);
            dataQueryService.save(s6);
            Operator q1 = QueryParser.stringToQuery("AND(EQUAL(timestamp,1555832341),GREATER_THAN(views,5))");
            Operator q2 = QueryParser.stringToQuery("AND(EQUAL(id,\"s1\"),LESS_THAN(views,200))");
            Operator q3 = QueryParser.stringToQuery("GREATER_THAN(views,20)");
//            Operator q4 = QueryParser.stringToQuery("GREATER_THAN(views,\"first-post\")");
            Operator q5 = QueryParser.stringToQuery("AND(NOT(EQUAL(views,500)),NOT(LESS_THAN(views,200)))");
            Operator q6 = QueryParser.stringToQuery("NOT(LESS_THAN(views,200))");
            List<Item> list = db.performQuery(q3);
            System.out.println("successful");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Item> query(String query) throws Exception {
        Operator q = QueryParser.stringToQuery(query);
        return Database.getInstance().performQuery(q);
    }

    public void save(Item Item) throws Exception {
        Database.getInstance().insert(Item);
    }
}
