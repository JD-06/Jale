package com.jvr.im;

public class DocsProfile {
    private String Title;
    private String Url;

    public DocsProfile() {
    }

    public DocsProfile(String title, String url) {
        Title = title;
        Url = url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
