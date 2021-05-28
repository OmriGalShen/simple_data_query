package service.Parser;

import api.CompareOperator;
import api.Operator;
import api.QueryParseException;

public class ParserUtility {
    static String[] processParams(String paramsString){
        String res = cutEdges(paramsString,'(',')'); // "(views,100)" -> "views,100"
       return res.split(",(?![^()]*\\))"); // "views,100" -> ["views","100"]
    }

     static String cutEdges(String str,char left,char right){
        if(str.charAt(0)==left&&str.charAt(str.length()-1)==right) {
            return str.substring(1,str.length()-1);
        }
        return str;
    }

    static Operator.Command stringToOperand(String op) throws QueryParseException {
        switch (op){
            case "EQUAL": return Operator.Command.EQUAL;
            case "GREATER_THAN": return Operator.Command.GREATER_THAN;
            case "LESS_THAN": return Operator.Command.LESS_THAN;
            case "AND": return Operator.Command.AND;
            case "OR": return Operator.Command.OR;
            case "NOT": return Operator.Command.NOT;
            case "BETWEEN": return Operator.Command.BETWEEN;
            case "UPDATE": return Operator.Command.UPDATE;
            case "DELETE": return Operator.Command.DELETE;
            case "MAX": return Operator.Command.MAX;
            case "MIN": return Operator.Command.MIN;
            case "ALL": return Operator.Command.ALL;
            default: throw new QueryParseException("Invalid query: Invalid Operator",new Throwable("Error in stringToOperand method"));
        }
    }

    static CompareOperator.CompareProperty stringToCompareProperty(String op) throws QueryParseException {
        switch (op){
            case "id":return CompareOperator.CompareProperty.id;
            case "title":return CompareOperator.CompareProperty.title;
            case "content":return CompareOperator.CompareProperty.content;
            case "views":return CompareOperator.CompareProperty.views;
            case "timestamp":return CompareOperator.CompareProperty.timestamp;
            default: throw new QueryParseException("Invalid query: Invalid CompareProperty",new Throwable("Error in stringToCompareProperty method"));
        }
    }

    static int getInteger(String value) throws QueryParseException{
        try{
            return Integer.parseInt(value);
        }
        catch (Exception e){
            throw new QueryParseException("Invalid query: Invalid value for comparison",e);
        }
    }

}
