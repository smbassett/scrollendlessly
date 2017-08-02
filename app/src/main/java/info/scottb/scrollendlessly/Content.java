package info.scottb.scrollendlessly;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Models the images and text that the user sees in each row of the RecyclerView.
 */

public class Content {

    private String title;
    private String description;
    private String imageUrl;
    private boolean imageOnly; // flag for whether this item only contains an image

    public Content(String imageUrl) {
        this.imageUrl = imageUrl;
        imageOnly = true;
    }

    public Content(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        imageOnly = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isImageOnly() {
        return imageOnly;
    }

    /** Convenience method for generating sample data locally. Useful for testing.
     * @param numItems number of Content items to generate
     * @return a list of Content objects
     */
    public static ArrayList<Content> generateSampleData(int numItems) {
        ArrayList<Content> sampleData = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            Content content;
            // About every 5th item should be a large image
            if (i == 1 || i > 0 && i % 5 == 0) {
                String imageUrl = "http://lorempixel.com/800/600/city/" + (int) (Math.random() * 11) + "/";
                content = new Content(imageUrl);
            } else {
                String title = "Title " + i;
                String description = "Lorem ipsum " + i + ", dolor sit amet, " +
                        "consectetur adipiscing elit. Aliquam vel tellus fermentum elit " +
                        "hendrerit maximus eget vel nulla.";
                String imageUrl = "http://lorempixel.com/400/300/city/" + (int) (Math.random() * 11) + "/";
                content = new Content(title, description, imageUrl);
            }

            sampleData.add(content);
        }

        return sampleData;
    }

    /**
     * Generates a list of Content objects from a JSON string.
     * @param jsonString JSON representation of Content objects
     * @return a list of Content objects
     */
    public static List<Content> fromJson(String jsonString) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Content>>(){}.getType();
        return gson.fromJson(jsonString, collectionType);
    }

}
