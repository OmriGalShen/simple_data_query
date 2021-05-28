package service.Parser;

/**
 * Simple Class used to store temporary string query information for
 * further parser processing
 * e.g: "GREATER_THAN(views,5))" -> op = "GREATER_THAN", params = ["views","5"]
 */
class StringQuery {
    String op;
    String[] params;

    public StringQuery(String op, String[] params) {
        this.op = op;
        this.params = params;
    }
}
