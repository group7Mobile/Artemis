package com.example.artemis;

/*
 * Class for favourite object. Can be modified to add more details.
 */
public class Site {
    private String title;
    private String url;

    public Site(String title, String url) {
        this.title = title;
        this.url = url;
    }

    /*
     * Returns the name of the favourite listing.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Sets the name of the favourite listing.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * Returns the name of the favourite URL.
     */
    public String getUrl() {
        return url;
    }

    /*
     * Sets the name of the favourite URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}