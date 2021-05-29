package service.IO;

import api.Item;
import com.google.gson.Gson;

import java.io.*;

/**
 * Handle read/write of json files
 */
public class IOHandler {

    private final String inputPath,outputPath;

    public IOHandler(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public Item[] getInputItems(){
        Input input = null;
        try {
            input= getInputFromJson(inputPath);
            return input.getItems();
        } catch (IOException e) {
            System.out.println("IOException on input from json file");
            e.printStackTrace();
        }
        return new Item[0];
    }

    public void outputItems(Item[] items){
        try {
            itemsToJson(outputPath, items);
        } catch (IOException e) {
            System.out.println("IOException on output of Diary to json format");
            e.printStackTrace();
        }
    }

    private static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }

    private static void itemsToJson(String filePath, Item[] items) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(items, writer);
        }
    }
}
