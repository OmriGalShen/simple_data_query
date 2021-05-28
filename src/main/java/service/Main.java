package service;

import api.*;
import service.Database.Database;
import service.IO.IOHandler;

import javax.swing.text.html.parser.Parser;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String inputFilePath = "input.json";
        String outputFilePath ="output.json";

        DataQueryService dataQueryService = new DataQueryServiceImpl();
        Scanner scan = new Scanner(System.in);

        IOHandler handler = new IOHandler(inputFilePath,outputFilePath);
        Item[] items = handler.getInputItems();
        for(Item item:items)
            dataQueryService.save(item);

        while (true) {
            System.out.print("Enter valid query (e.g:LESS_THAN(views,150)): ");
            String query = scan.nextLine();  // Read user input
            try {
                Item[] outputItems = dataQueryService.query(query).toArray(new Item[0]);
                System.out.println(outputItems.length + " results were found (out of " + items.length + " items)");
                System.out.println("Results saved to output.json");
                System.out.print("Display results (y/n)? ");
                String answer = scan.nextLine();
                if (answer.equals("y")) {
                    System.out.println("Output items: ");
                    System.out.println(Arrays.toString(outputItems));
                }
                handler.outputItems(outputItems);
            }
            catch (QueryParseException e){
                System.out.println(e.getMessage());
            }
        }
    }

}
