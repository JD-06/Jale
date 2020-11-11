package com.jvr.im;

public class HomeTalentProfile {
    private String id;
    private String title;
    private int Postulantes;
    private String date;
    private String name;

    public HomeTalentProfile() {
    }

    public HomeTalentProfile(String id, String title, int postulantes, String date, String name) {
        this.id = id;
        this.title = title;
        Postulantes = postulantes;
        this.date = date;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPostulantes() {
        return Postulantes;
    }

    public void setPostulantes(int postulantes) {
        Postulantes = postulantes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
