package service;

import api.*;
import service.Database.Database;
import service.IO.IOHandler;

import java.util.Arrays;
import java.util.Locale;
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
            System.out.println("tip: type 'help' for query options or 'exit' to abort");
            System.out.print("Enter valid query: ");
            String query = scan.nextLine();
            if (query.equalsIgnoreCase("help")) {
                printHelp();
                scan.nextLine();
            }
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
                    "| EQUAL(property,value)  Filters only values which have matching property value.| EQUAL(id,\\\"first-post\\\")  EQUAL(views,100)   | \n" +
                    "| GREATER_THAN(property,value)  Filters only values for which property is greater than the given value. Valid only for number values.| GREATER_THAN(views,100) | \n" +
                    "| LESS_THAN(property,value)  Filters only values for which property is less than the given value. Valid only for number values.| LESS_THAN(views,100) | \n" +
                    "| AND(a,b)  Filters only values for which both a and b are true.| AND(EQUAL(id,\\\"first-post\\\"),EQUAL(views,100)) | \n" +
                    "| OR(a,b)  Filters only values for which either a or b is true (or both).| OR(EQUAL(id,\\\"first-post\\\"),EQUAL(id,\\\"second-post\\\")) | \n" +
                    "| NOT(a)  Filters only values for which a is false.| NOT(EQUAL(id,\\\"first-post\\\")) | \n" +
                    "| BETWEEN(property,value,value)   Filters only values for which property is between the given value. Valid only for number values.| BETWEEN(views,20,100) | \n" +
                    "| UPDATE(property,value,a)   Update property where a is true| UPDATE(EQUAL(id,\\\"first-post\\\"),views,500) | \n" +
                    "| DELETE(a)   Delete values where a is true| DELETE(EQUAL(id,\\\"first-post\\\")) | \n" +
                    "| MAX(property,a)   Filters only value for which property is the maximum, valid only for number property and where a is true| MAX(views,EQUAL(title,\\\"My First Post\\\")) | \n" +
                    "| MIN(property,a)   Filters only value for which property is the minimum, valid only for number property and where a is true | MIN(views,GREATER_THAN(views,20)) | \n" +
                    "| ALL()   Return all values | ALL() |");
        System.out.println("Press enter to continue");
    }

}
