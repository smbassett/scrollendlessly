package info.scottb.scrollendlessly;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContentTest {

    public ArrayList<Content> sampleData;

    @Before
    public void generateSampleData() {
        sampleData = Content.generateSampleData(30);
    }

    @Test
    public void shouldGenerateValidSampleData() throws Exception {
        assertEquals(30, sampleData.size());

        assertEquals("Title 0", sampleData.get(0).getTitle());
        assertEquals("Lorem ipsum 0, dolor sit amet, " +
                "consectetur adipiscing elit. Aliquam vel tellus fermentum elit " +
                "hendrerit maximus eget vel nulla.", sampleData.get(0).getDescription());
        assertTrue(sampleData.get(0).getImageUrl().startsWith("http://lorempixel.com/400/300/city/"));
        assertFalse(sampleData.get(0).isImageOnly());
    }

    @Test
    public void shouldGenerateValidImageOnlyContent() throws Exception {
        assertTrue(sampleData.get(1).isImageOnly());
        assertTrue(sampleData.get(1).getTitle() == null);
        assertTrue(sampleData.get(1).getDescription() == null);
        assertTrue(sampleData.get(1).getImageUrl().startsWith("http://lorempixel.com/800/600/city/"));
    }

    @Test
    public void shouldParseJsonIntoContentList() throws Exception {
        String json = "[\n" +
                "  {\n" +
                "    \"title\": \"Labore Eiusmod\",\n" +
                "    \"description\": \"Velit consequat pariatur adipisicing consectetur eu ut ea anim sit ipsum officia.\",\n" +
                "    \"imageUrl\": \"http://lorempixel.com/400/300/city/a/\",\n" +
                "    \"imageOnly\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"imageUrl\": \"http://lorempixel.com/800/600/city/b/\",\n" +
                "    \"imageOnly\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"title\": \"Elit Ex\",\n" +
                "    \"description\": \"Aliquip enim aute incididunt id in eu excepteur dolor elit culpa veniam.\",\n" +
                "    \"imageUrl\": \"http://lorempixel.com/400/300/city/c/\",\n" +
                "    \"imageOnly\": false\n" +
                "  }\n" +
                "]";

        List<Content> dataFromJson = Content.fromJson(json);

        assertEquals(3, dataFromJson.size());
        assertEquals("Labore Eiusmod", dataFromJson.get(0).getTitle());
        assertEquals("Velit consequat pariatur adipisicing consectetur eu ut ea anim sit ipsum officia.", dataFromJson.get(0).getDescription());
        assertEquals("http://lorempixel.com/400/300/city/a/", dataFromJson.get(0).getImageUrl());
        assertFalse(dataFromJson.get(0).isImageOnly());
        assertTrue(dataFromJson.get(1).isImageOnly());
    }
}