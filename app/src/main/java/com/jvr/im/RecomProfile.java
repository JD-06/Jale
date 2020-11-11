package com.jvr.im;

public class RecomProfile {
    private String name;
    private String recomm;

    public RecomProfile() {
    }

    public RecomProfile(String name, String recomm) {
        this.name = name;
        this.recomm = recomm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecomm() {
        return recomm;
    }

    public void setRecomm(String recomm) {
        this.recomm = recomm;
    }
}
