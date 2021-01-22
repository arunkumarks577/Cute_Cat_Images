package com.example.catimages;

public class Cats {

    private String id;
    private String url;

    public Cats(String id, String url)
    {
        this.id= id;
        this.url= url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
