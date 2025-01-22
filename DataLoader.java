package com.recommendationsystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<String[]> loadCSV(String fileName, String delimiter, boolean hasHeader) {
        List<String[]> data = new ArrayList<>();
        try (InputStream inputStream = DataLoader.class.getResourceAsStream("/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new RuntimeException("File not found: " + fileName);
            }

            String line;
            if (hasHeader) reader.readLine(); // Skip the header if exists
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(delimiter);
                data.add(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading file: " + fileName);
        }
        return data;
    }

    public static void main(String[] args) {
        List<String[]> ratings = loadCSV("ratings.csv", ",", true);
        System.out.println("Loaded " + ratings.size() + " rows from ratings.csv.");

        for (int i = 0; i < Math.min(5, ratings.size()); i++) {
            System.out.println(String.join(" ", ratings.get(i)));
        }
    }
}
