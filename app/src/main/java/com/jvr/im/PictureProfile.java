package com.jvr.im;

public class PictureProfile {
    private String name;
    private int likes;
    private String title;
    private String date;
    private String picturelink;
    private String id;

    public PictureProfile() {
    }

    public PictureProfile(String name, int likes, String title, String date, String picturelink, String id) {
        this.name = name;
        this.likes = likes;
        this.title = title;
        this.date = date;
        this.picturelink = picturelink;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicturelink() {
        return picturelink;
    }

    public void setPicturelink(String picturelink) {
        this.picturelink = picturelink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
