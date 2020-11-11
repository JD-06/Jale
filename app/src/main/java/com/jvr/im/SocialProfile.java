package com.jvr.im;

public class SocialProfile {
    private String Url;
    private String Social;

    public SocialProfile() {
    }

    public SocialProfile(String url, String social) {
        Url = url;
        Social = social;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getSocial() {
        return Social;
    }

    public void setSocial(String social) {
        Social = social;
    }
}
