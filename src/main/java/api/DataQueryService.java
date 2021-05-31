package api;

import java.util.List;

/**
 * Interface used for representing a simple service able to store and retrieve data.
 */
public interface DataQueryService {
    List<Item> query(String query) throws Exception;

    void save(Item Item) throws Exception;
}

