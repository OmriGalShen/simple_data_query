package service.Parser;

import api.*;
import api.CompareOperator;
import api.Operator;
import service.Operators.*;

import static service.Parser.ParserUtility.*;

/**
 The main parser class.
 The class supplies a static method to process
 Strings in agreed-upon query form to Operator objects used to manipulate data
 **/

public class QueryParser {
    /**
     * Tha main method supplied by the parser.
     * Convert String in correct query form to appropriate Operator object
     * @param sQuery String with correct query format e.g: "NOT(EQUAL(views,500))"
     * @return Appropriate Operator object representing the info needed to apply the query
     * @throws QueryParseException Any error occurred during the process will raise appropriate QueryParseException exception
     */
    public static Operator stringToQuery(String sQuery) throws QueryParseException {
        if(sQuery.isEmpty())return new EmptyOperator(); // edge case: empty Query

        int firstPare = sQuery.indexOf('(');
        if(firstPare == -1||sQuery.charAt(sQuery.length()-1)!=')')
            throw new QueryParseException("Invalid query: missing parentheses",new Throwable("Error in stringToQuery method"));

        String cmd = sQuery.substring(0,firstPare); // "GREATER_THAN(views,100))" -> "GREATER_THAN"
        String paramsString = sQuery.substring(firstPare); // "GREATER_THAN(views,100))" -> "(views,100)"
        String[] params = processParams(paramsString); // "(views,100)" -> ["views","100"]

        StringQuery sq = new StringQuery(cmd,params); // e.g: "GREATER_THAN(views,5))" -> op = "GREATER_THAN", params = ["views","5"]
        return createOperator(sq);
    }

    private static Operator createOperator(StringQuery sq) throws QueryParseException {
        if(!Operator.isCommand(sq.op))
            throw new QueryParseException("Invalid query: Invalid Operator",new Throwable("Error in createOperator method"));
        if (BooleanOperator.isBooleanCommand(sq.op)) return createBooleanOperator(sq); // AND, OR, NOT
        if (CompareOperator.isCompareCommand(sq.op)) return createCompareOperator(sq); // EQUAL, GREATER_THAN, LESS_THAN
        if(ModifyOperator.isModifyCommand(sq.op)) return createModifyOperator(sq);
        throw new QueryParseException("Invalid query: Invalid Operator",new Throwable("Error in createOperator method"));
    }

    private static Operator createBooleanOperator(StringQuery sq) throws QueryParseException{ // AND, OR, NOT
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        Throwable error = new Throwable("Error in createBooleanOperator method");

        switch (op){
            case NOT:
                if(sq.params.length!=1)
                    throw new QueryParseException(paramsErrorMsg("NOT",1,sq.params.length),error);
                return new NotOperator(stringToQuery(sq.params[0]));
            case AND:
                if(sq.params.length!=2)
                    throw new QueryParseException(paramsErrorMsg("AND",2,sq.params.length),error);
                return new AndOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case OR:
                if(sq.params.length!=2)
                    throw new QueryParseException(paramsErrorMsg("AND",2,sq.params.length),error);
                return new OrOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case BETWEEN:
                if(sq.params.length!=3)
                    throw new QueryParseException(paramsErrorMsg("BETWEEN",3,sq.params.length),error);
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createBooleanOperator method"));
    }

    private static Operator createModifyOperator(StringQuery sq) throws QueryParseException{ // EQUAL, GREATER_THAN, LESS_THAN BETWEEN
        String id = cutEdges(sq.params[0],'\"','\"');
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        switch (op){
            case UPDATE:
                if(sq.params.length!=3)
                    throw new QueryParseException(paramsErrorMsg("UPDATE",3,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                CompareOperator.CompareProperty cp = stringToCompareProperty(sq.params[1]); // convert to appropriate enum, can throw
                if(cp.equals(CompareOperator.CompareProperty.views)||cp.equals(CompareOperator.CompareProperty.timestamp)) // Integer Property
                    return new UpdateOperator<>(op,id,cp,getInteger(sq.params[2])); // can throw
                return new UpdateOperator<>(op,id,cp,cutEdges(sq.params[2],'\"','\"')); // String Property

            case DELETE:
                if(sq.params.length!=1)
                    throw new QueryParseException(paramsErrorMsg("DELETE",1,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                return new DeleteOperator(op,id);
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createCompareOperator method"));
    }

    private static Operator createCompareOperator(StringQuery sq) throws QueryParseException{ // UPDATE DELETE
        String property = sq.params[0]; // ["views","100"] -> "views"
        String value = sq.params[1]; // ["views","100"] -> "100"

        CompareOperator.CompareProperty cp = stringToCompareProperty(property); // convert to appropriate enum, can throw
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        switch (op){
            case EQUAL:
                if(sq.params.length!=2)
                    throw new QueryParseException(paramsErrorMsg("EQUAL",2,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                if(cp.equals(CompareOperator.CompareProperty.views)||cp.equals(CompareOperator.CompareProperty.timestamp)) // Integer Property
                    return new EqualOperator<>(cp,getInteger(value)); // can throw
                return new EqualOperator<>(cp,cutEdges(value,'\"','\"')); // String Property
            case GREATER_THAN:
                if(sq.params.length!=2)
                    throw new QueryParseException(paramsErrorMsg("GREATER_THAN",2,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                return new GreaterThenOperator(cp,getInteger(value)); // can throw
            case LESS_THAN:
                if(sq.params.length!=2)
                    throw new QueryParseException(paramsErrorMsg("LESS_THAN",2,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                return new LessThenOperator(cp,getInteger(value)); // can throw
            case BETWEEN:
                if(sq.params.length!=3)
                    throw new QueryParseException(paramsErrorMsg("BETWEEN",3,sq.params.length),
                            new Throwable("Error in createCompareOperator method"));
                return new BetweenOperator(cp,getInteger(value),getInteger(sq.params[2]));
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createCompareOperator method"));
    }

}
