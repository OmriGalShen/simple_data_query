package service.Parser;

import api.*;
import api.CompareOperator;
import api.Operator;
import service.Operators.*;

import static service.Parser.ParserUtility.*;

/**
 The main parser class.
 The class supplies a static method to process
 Strings in agreed-upon query form to Operator objects
 used to retrieve and manipulate database data
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
        if (BooleanOperator.isBooleanCommand(sq.op)) // AND, OR, NOT, ALL
            return createBooleanOperator(sq);
        if (CompareOperator.isCompareCommand(sq.op))// EQUAL, GREATER_THAN, LESS_THAN, BETWEEN, MAX, MIN
            return createCompareOperator(sq);
        if(ModifyOperator.isModifyCommand(sq.op)) // UPDATE, DELETE
            return createModifyOperator(sq);
        throw new QueryParseException("Invalid query: Invalid Operator",new Throwable("Error in createOperator method"));
    }

    private static Operator createBooleanOperator(StringQuery sq) throws QueryParseException{ // AND, OR, NOT ALL
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        int paramLen = sq.params.length;

        switch (op){
            case NOT: //NOT(a)
                checkParamCount(sq.op,1,paramLen);
                return new NotOperator(stringToQuery(sq.params[0]));
            case AND: //AND(a,b)
                checkParamCount(sq.op,2,paramLen);
                return new AndOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case OR: //OR(a,b)
                checkParamCount(sq.op,2,paramLen);
                return new OrOperator(stringToQuery(sq.params[0]),stringToQuery(sq.params[1]));
            case ALL: //ALL()
                checkParamCount(sq.op,1,paramLen);
                return new AllOperator();
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createBooleanOperator method"));
    }

    private static Operator createCompareOperator(StringQuery sq) throws QueryParseException{ /// EQUAL, GREATER_THAN, LESS_THAN, BETWEEN, MAX, MIN
        String property = sq.params[0]; // ["views","100"] -> "views"
        String value = sq.params[1]; // ["views","100"] -> "100"

        Operator.CompareProperty cp = stringToCompareProperty(property); // convert to appropriate enum, can throw
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        int paramLen = sq.params.length;

        switch (op){
            case EQUAL: //EQUAL(property,value)
                checkParamCount(sq.op,2,paramLen);
                if(isIntProperty(cp))
                    return new EqualOperator<>(cp,getInteger(value)); // can throw
                return new EqualOperator<>(cp,getString(value)); // String Property
            case GREATER_THAN: //GREATER_THAN(property,value)
                checkParamCount(sq.op,2,paramLen);
                if(isIntProperty(cp))
                    return new GreaterThenOperator(cp,getInteger(value)); // can throw
                break;
            case LESS_THAN: //LESS_THAN(property,value)
                checkParamCount(sq.op,2,paramLen);
                if(isIntProperty(cp))
                     return new LessThenOperator(cp,getInteger(value)); // can throw
                break;
            case BETWEEN: //BETWEEN(property,value,value)
                checkParamCount(sq.op,3,paramLen);
                String secondValue = sq.params[2];
                if(isIntProperty(cp))
                    return new BetweenOperator(cp,getInteger(value),getInteger(secondValue));
                break;
            case MAX: //MAX(property,a)
                checkParamCount(sq.op,2,paramLen);
                if(isIntProperty(cp))
                    return new MaxOperator(cp,stringToQuery(value)); // can throw
                break;
            case MIN: //MIN(property,a)
                checkParamCount(sq.op,2,paramLen);
                if(isIntProperty(cp))
                    return new MinOperator(cp,stringToQuery(value)); // can throw
                break;
        }
        throw new QueryParseException("Invalid query",new Throwable("Error in createCompareOperator method"));
    }

    private static Operator createModifyOperator(StringQuery sq) throws QueryParseException{ // UPDATE, DELETE
        Operator.Command op = stringToOperand(sq.op); // convert to appropriate enum, can throw

        int paramLen = sq.params.length;

        switch (op){
            case DELETE: //DELETE(a)
                checkParamCount(sq.op,1,paramLen);
                return new DeleteOperator(stringToQuery(sq.params[0]));
            case UPDATE: //UPDATE(a,property,value)
                String query = sq.params[0];
                checkParamCount(sq.op,3,paramLen);
                Operator.CompareProperty cp = stringToCompareProperty(sq.params[1]); // convert to appropriate enum, can throw
                String value = sq.params[2];
                if(isIntProperty(cp))
                    return new UpdateOperator<>(stringToQuery(query),cp,getInteger(value)); // can throw
                return new UpdateOperator<>(stringToQuery(query),cp,getString(value));
        }

        throw new QueryParseException("Invalid query",new Throwable("Error in createModifyOperator method"));
    }



}
