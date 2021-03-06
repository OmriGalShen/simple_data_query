import api.DataQueryService;
import api.Item;
import api.NoDataFound;
import api.QueryParseException;
import org.junit.Test;
import service.DataQueryServiceImpl;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DataQueryServiceTest {

    DataQueryService dataQueryService = new DataQueryServiceImpl();
    private Item templateItem = new Item("first-post", "My First Post", "Hello World!", 1, 1555832341);

    @Test
    public void SaveAndResponseWithItem() throws Exception {

        dataQueryService.save(templateItem);
        assertEquals(true, dataQueryService.query("").contains(templateItem));
    }

    @Test
    public void FilterItemBaseOnSimpleEqualQueryString() throws Exception {

        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        assertEquals(true, dataQueryService.query("EQUAL(id,\"id2\")").contains(item2));
    }

    @Test
    public void FilterItemBaseOnSimpleLessThanFilter() throws Exception {
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        item1.setViews(41);
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        item2.setViews(50);

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        assertEquals(true, dataQueryService.query("LESS_THAN(views,42)").contains(item1));
    }

    @Test
    public void FilterItemBaseOnSimpleGreaterThanFilter() throws Exception {
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        item1.setViews(41);
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        item2.setViews(50);

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        assertEquals(true, dataQueryService.query("GREATER_THAN(views,41)").contains(item2));

    }

    @Test
    public void FilterItemBaseOnComplexAndQueryString() throws Exception {
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");
        item3.setViews(101);


        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("AND(EQUAL(id,\"id3\"),GREATER_THAN(views,100))").containsAll(Arrays.asList(item3)));
    }

    @Test
    public void FilterItemBaseOnComplexOrQueryString() throws Exception {
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");


        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("OR(EQUAL(id,\"id1\"),EQUAL(id,\"id2\"))").containsAll(Arrays.asList(item1, item2)));
    }

    @Test(expected = QueryParseException.class)
    public void ReturnErrorWhenInvalidQueryPassed() throws Exception {
        dataQueryService.query("INVALID");
    }

    @Test
    public void testUpdateOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        item1.setViews(500);
        dataQueryService.save(item1);
        dataQueryService.query("UPDATE(EQUAL(id,\"id1\"),views,1000)");
        assertEquals(true, dataQueryService.query("EQUAL(id,\"id1\")").get(0).getViews()==1000);
    }

    @Test(expected = NoDataFound.class)
    public void testDeleteOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        dataQueryService.save(item1);
        dataQueryService.query("DELETE(EQUAL(id,\"id1\"))");
        assertEquals(true, dataQueryService.query("").containsAll(Arrays.asList(item2)));
    }

    @Test
    public void testBetweenOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setViews(1);
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        item2.setViews(50);
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");
        item3.setViews(20);

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("BETWEEN(views,5,100)").containsAll(Arrays.asList(item2,item3)));
    }

    @Test
    public void testMinOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setViews(1);
        item1.setTitle("t");
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        item2.setTitle("t");
        item2.setViews(50);
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");
        item3.setTitle("t");
        item3.setViews(20);

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("MIN(views,EQUAL(title,\"t\"))").containsAll(Arrays.asList(item1)));
    }

    @Test
    public void testMaxOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        item1.setTitle("t");
        item1.setViews(1);
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        item2.setTitle("t");
        item2.setViews(50);
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");
        item3.setTitle("t");
        item3.setViews(20);

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("MAX(views,EQUAL(title,\"t\"))").containsAll(Arrays.asList(item2)));
    }

    @Test
    public void testAllOperator() throws Exception{
        Item item1 = (Item) templateItem.clone();
        item1.setId("id1");
        Item item2 = (Item) templateItem.clone();
        item2.setId("id2");
        Item item3 = (Item) templateItem.clone();
        item3.setId("id3");

        dataQueryService.save(item1);
        dataQueryService.save(item2);
        dataQueryService.save(item3);
        assertEquals(true, dataQueryService.query("ALL()").containsAll(Arrays.asList(item1, item2,item3)));
    }
}
