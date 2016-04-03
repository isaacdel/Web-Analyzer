/**
 * A model holding the site's key strings
 */
public class SiteData {
    String title;
    String h1;
    String h2;
    String metaTagDescription;

    public String getMetaTagTwitterDescription() {
        return metaTagTwitterDescription;
    }

    public void setMetaTagTwitterDescription(String metaTagTwitterDescription) {
        this.metaTagTwitterDescription = metaTagTwitterDescription;
    }

    String metaTagTwitterDescription;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }
    public String getMetaTagDescription() {
        return metaTagDescription;
    }

    public void setMetaTagDescription(String metaTagDescription) {
        this.metaTagDescription = metaTagDescription;
    }
}
