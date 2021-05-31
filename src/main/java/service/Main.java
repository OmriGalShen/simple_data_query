package service;

import api.*;
import service.Database.Database;
import service.IO.IOHandler;

import java.util.Arrays;
import java.util.Scanner;

/**
 * A simple command line interface used to run query commands
 */
public class Main {
    public static void main(String[] args) throws Exception {

        String inputFilePath = "input.json"; // default input path
        String outputFilePath ="output.json"; // default output path

        DataQueryService dataQueryService = new DataQueryServiceImpl();
        Scanner scan = new Scanner(System.in);

        IOHandler handler = new IOHandler(inputFilePath,outputFilePath);
        Item[] inputItems = handler.getInputItems();
        for(Item item:inputItems)
            dataQueryService.save(item); // save all input items to database

        boolean keepLoop = true;

        while (keepLoop) {
            System.out.println("tip: type 'help' for query list or 'exit' to abort");
            System.out.print("Enter valid query: ");
            String query = scan.nextLine().trim();
            if (query.equalsIgnoreCase("help")) printHelp();
            else if(query.equalsIgnoreCase("exit")) keepLoop = false;
            else {
                try {
                    Item[] outputItems = dataQueryService.query(query).toArray(new Item[0]);
                    System.out.println(outputItems.length + " results were found (out of " + Database.getInstance().getAllKeysSet().size() + " items)");
                    System.out.println("Results saved to output.json");
                    System.out.print("Display results (y/n)? ");
                    String answer = scan.nextLine();
                    if (answer.equals("y")) {
                        System.out.println("Output items: ");
                        System.out.println(Arrays.toString(outputItems));
                    }
                    handler.outputItems(outputItems);
                } catch (QueryParseException | NoDataFound e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void printHelp(){
        System.out.println(
                    "---------------------------------------------------\n" +
                    "Operator:EQUAL(property,value)\nDescription:Filters only values which have matching property value.\nExample:EQUAL(id,\"s1\") \n" +
                            "---------------------------------------------------\n" +
                            "Operator:GREATER_THAN(property,value)\nDescription:Filters only values for which property is greater than the given value. Valid only for number values.\nExample:GREATER_THAN(views,100) \n" +
                            "---------------------------------------------------\n" +
                            "Operator:LESS_THAN(property,value)\nDescription:Filters only values for which property is less than the given value. Valid only for number values.\nExample:LESS_THAN(views,100) \n" +
                            "---------------------------------------------------\n" +
                            "Operator:AND(a,b)\nDescription:Filters only values for which both a and b are true.\nExample:AND(GREATER_THAN(views,70),EQUAL(title,\"My Second Post\"))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:OR(a,b)\nDescription:Filters only values for which either a or b is true (or both).\nExample:OR(EQUAL(id,\"s1\"),EQUAL(id,\"s2\"))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:NOT(a)\nDescription:Filters only values for which a is false.\nExample:NOT(EQUAL(id,\"s1\"))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:BETWEEN(property,value,value)\nDescription:Filters only values for which property is between the given values. Valid only for number values.\nExample:BETWEEN(views,20,100)\n" +
                            "---------------------------------------------------\n" +
                            "Operator:UPDATE(a,property,value)\nDescription:Update property where a is true.\nExample:UPDATE(EQUAL(id,\"s1\"),views,500)\n" +
                            "---------------------------------------------------\n" +
                            "Operator:DELETE(a)\nDescription:Delete values where a is true.\nExample:DELETE(EQUAL(id,\"s1\"))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:MAX(property,a)\nDescription:Filters only value for which property is the maximum, valid only for number property and where a is true.\nExample:MAX(views,EQUAL(title,\"My First Post\"))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:MIN(property,a)\nDescription:Filters only value for which property is the minimum, valid only for number property and where a is true.\nExample:MIN(views,GREATER_THAN(views,20))\n" +
                            "---------------------------------------------------\n" +
                            "Operator:ALL()\nDescription:Return all values.\nExample:ALL()\n" +
        "---------------------------------------------------\n");
    }

}
