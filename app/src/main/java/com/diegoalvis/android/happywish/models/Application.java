package com.diegoalvis.android.happywish.models;

import java.io.Serializable;

/**
 * Created by diegoalvis on 1/23/17.
 */

public class Application implements Serializable { // Implement serializable to be cached

    private int id;
    private String name, title, summary, rights, link, date, image, currency;
    private double price;
    private Category category;


    public Application(int id, String name, String title, String summary, String rights, String link, String date, String image, String currency, double price, Category category) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.summary = summary;
        this.rights = rights;
        this.link = link;
        this.date = date;
        this.image = image;
        this.currency = currency;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
