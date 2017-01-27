package com.diegoalvis.android.happywish.models;

import java.io.Serializable;

/**
 * Created by diegoalvis on 1/23/17.
 */


public class Category implements Serializable { // Implement serializable to be cached

    private int id;
    private String category, url;

    public Category(int id, String category, String url) {
        this.id = id;
        this.category = category;
        this.url = url;
    }

    public Category() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
