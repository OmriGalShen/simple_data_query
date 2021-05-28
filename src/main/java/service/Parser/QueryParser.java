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
        if(sQuery.isEmpty())return new AllOperator(); // edge case: empty Query

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
        throw new QueryParseException("Invalid query: Invalid Operator",new Throwable("Error in createOperator method"));
    }

    private static Operator createBooleanOperator(StringQuery sq) throws QueryParseException{ // AND, OR, NOT
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        int paramLen = sq.params.length;

        switch (op){
            case NOT:
                checkParam(sq.op,1,paramLen);
                return new NotOperator(stringToQuery(sq.params[0]));
            case AND:
                checkParam(sq.op,2,paramLen);
                return new AndOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case OR:
                checkParam(sq.op,2,paramLen);
                return new OrOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case DELETE:
                checkParam(sq.op,1,paramLen);
                return new DeleteOperator(stringToQuery(sq.params[0]));
            case ALL:
                checkParam(sq.op,1,paramLen);
                return new AllOperator();
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createBooleanOperator method"));
    }

    private static Operator createCompareOperator(StringQuery sq) throws QueryParseException{ // UPDATE DELETE
        String property = sq.params[0]; // ["views","100"] -> "views"
        String value = sq.params[1]; // ["views","100"] -> "100"

        CompareOperator.CompareProperty cp = stringToCompareProperty(property); // convert to appropriate enum, can throw
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        boolean isIntProp = cp.equals(CompareOperator.CompareProperty.views) || cp.equals(CompareOperator.CompareProperty.timestamp);

        int paramLen = sq.params.length;

        switch (op){
            case EQUAL:
                checkParam(sq.op,2,paramLen);
                if(isIntProp) // Integer Property
                    return new EqualOperator<>(cp,getInteger(value)); // can throw
                return new EqualOperator<>(cp,cutEdges(value,'\"','\"')); // String Property
            case GREATER_THAN:
                checkParam(sq.op,2,paramLen);
                if(isIntProp) // Integer Property
                    return new GreaterThenOperator(cp,getInteger(value)); // can throw
                break;
            case LESS_THAN:
                checkParam(sq.op,2,paramLen);
                if(isIntProp) // Integer Property
                     return new LessThenOperator(cp,getInteger(value)); // can throw
                break;
            case BETWEEN:
                checkParam(sq.op,3,paramLen);
                if(isIntProp) // Integer Property
                    return new BetweenOperator(cp,getInteger(value),getInteger(sq.params[2]));
                break;
            case UPDATE:
                checkParam(sq.op,3,paramLen);
                if(isIntProp) // Integer Property
                    return new UpdateOperator<>(cp,getInteger(value),stringToQuery(sq.params[2])); // can throw
                return new UpdateOperator<>(cp,cutEdges(value,'\"','\"'),stringToQuery(sq.params[2])); // String Property
            case MAX:
                checkParam(sq.op,2,paramLen);
                if(isIntProp) // Integer Property
                    return new MaxOperator(cp,stringToQuery(value)); // can throw
                break;
            case MIN:
                checkParam(sq.op,2,paramLen);
                if(isIntProp) // Integer Property
                    return new MinOperator(cp,stringToQuery(value)); // can throw
                break;
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createCompareOperator method"));
    }

    private static void checkParam(String op, int expected,int got) throws QueryParseException {
        if(expected!=got)
        throw new QueryParseException("Invalid query: Incorrect number of "+op+" parameters expected:"+expected+" got:"+got,
                new Throwable("Error in checkParam method"));
    }

}
