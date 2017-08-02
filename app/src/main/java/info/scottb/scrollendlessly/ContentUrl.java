package info.scottb.scrollendlessly;

/**
 * Generates URLs for content to be fetched from the server.
 */

public class ContentUrl {

    /**
     * Returns a URL for the next page of data that needs to be fetched from the server when
     * endlessly scrolling
     * @param sectionNumber the section (or tab) being displayed
     * @param offset the number of 'pages' of data that have already been fetched
     * @return
     */
    public static String getUrl(int sectionNumber, int offset) {
        String section = getSectionName(sectionNumber);
        return "https://raw.githubusercontent.com/smbassett/smbassett/master/" + section + "/" + offset + ".json";
    }

    /**
     * Returns the name of the section based on the section number.
     * This is used to construct the URL where the content JSON for the section is located.
     * @param sectionNumber the section (or tab) that is being displayed
     * @return the name of that section
     */
    private static String getSectionName(int sectionNumber) {
        String section;
        switch (sectionNumber) {
            case 1:
                section = "city_guide";
                break;
            case 2:
                section = "shop";
                break;
            case 3:
                section = "eat";
                break;
            default:
                section = "general";
        }
        return section;
    }

}
