package com.recommendationsystem;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class RecommendationSystem {
    public static void main(String[] args) {
        try {
            
            URL resource = RecommendationSystem.class.getClassLoader().getResource("ml-latest-small/ratings.csv");
            if (resource == null) {
                throw new RuntimeException("File not found in resources folder.");
            }

            File file = new File(resource.toURI());
            File processedFile = preprocessCSVFile(file);

            DataModel dataModel = new FileDataModel(processedFile);
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);

            int userId = 1;
            int numberOfRecommendations = 5;
            List<RecommendedItem> recommendations = recommender.recommend(userId, numberOfRecommendations);

            System.out.println("Recommendations for user " + userId + ":");
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("Item: " + recommendation.getItemID() + ", Predicted Rating: " + recommendation.getValue());
            }

            if (processedFile.exists()) {
                processedFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File preprocessCSVFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        List<String> linesWithoutHeader = lines.subList(1, lines.size());
        File tempFile = File.createTempFile("processed-ratings", ".csv");
        Files.write(tempFile.toPath(), linesWithoutHeader);
        return tempFile;
    }
}
