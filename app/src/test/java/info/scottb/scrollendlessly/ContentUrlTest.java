package info.scottb.scrollendlessly;

import org.junit.Test;

import static info.scottb.scrollendlessly.ContentUrl.getUrl;
import static org.junit.Assert.assertEquals;

public class ContentUrlTest {

    @Test
    public void shouldGenerateCorrectUrl() throws Exception {
        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/city_guide/0.json", getUrl(1, 0));
        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/shop/0.json", getUrl(2, 0));
        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/eat/0.json", getUrl(3, 0));

        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/city_guide/1.json", getUrl(1, 1));
        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/city_guide/2.json", getUrl(1, 2));
        assertEquals("https://raw.githubusercontent.com/smbassett/smbassett/master/city_guide/3.json", getUrl(1, 3));
    }

}