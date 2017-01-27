package com.diegoalvis.android.happywish.utilities.managers;

import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diegoalvis on 1/23/17.
 *
 * Class abstract to manage responses
 * from list applications service
 */

public abstract class WebResponseManager {

    public static final String FEED_KEY = "feed";
    public static final String ENTRY_KEY = "entry";

    /**      Object key words     **/
    public static final String OBJ_ID = "id";
    public static final String OBJ_NAME = "im:name";
    public static final String OBJ_IMAGE = "im:image";
    public static final String OBJ_PRICE = "im:price";
    public static final String OBJ_RELASE_DATE = "im:releaseDate";
    public static final String OBJ_RIGHTS = "rights";
    public static final String OBJ_TITLE = "title";
    public static final String OBJ_SUMMARY = "summary";
    public static final String OBJ_CATEGORY = "category";
    public static final String OBJ_LINK = "link";

    /**      general key words   **/
    public static final String LABEL = "label";
    public static final String ATTRIBUTES = "attributes";

    /**     specific key words     **/
    public static final String CURRENCY = "currency";
    public static final String AMOUNT = "amount";
    public static final String HREF = "href";
    public static final String ID = "im:id";
    public static final String SCHEME = "scheme";

    
    public static List<Application> parseFromJSON(JsonObject objResponse) throws JSONException {
        List<Application> applications = new ArrayList<>();
        JsonArray entryArray = objResponse.getAsJsonObject(FEED_KEY).getAsJsonArray(ENTRY_KEY);

        if(entryArray.size() > 0) {

            Application app;
            Category category;
            JsonObject entry;

            for(int i=0; i<entryArray.size(); i++) {

                int id, idCategory;
                String name, title, summary, rights, link, date, image, currency, categoryName, url;
                double price;

                entry = entryArray.get(i).getAsJsonObject();

                id          = entry.getAsJsonObject(OBJ_ID).getAsJsonObject(ATTRIBUTES).get(ID).getAsInt();
                name        = entry.getAsJsonObject(OBJ_NAME).get(LABEL).getAsString();
                title       = entry.getAsJsonObject(OBJ_TITLE).get(LABEL).getAsString();
                summary     = entry.getAsJsonObject(OBJ_SUMMARY).get(LABEL).getAsString();
                rights      = entry.getAsJsonObject(OBJ_RIGHTS).get(LABEL).getAsString();
                link        = entry.getAsJsonObject(OBJ_LINK).getAsJsonObject(ATTRIBUTES).get(HREF).getAsString();
                date        = entry.getAsJsonObject(OBJ_RELASE_DATE).getAsJsonObject(ATTRIBUTES).get(LABEL).getAsString();
                image       = entry.get(OBJ_IMAGE).getAsJsonArray().get(2).getAsJsonObject().get(LABEL).getAsString();
                currency    = entry.getAsJsonObject(OBJ_PRICE).getAsJsonObject(ATTRIBUTES).get(CURRENCY).getAsString();
                price       = entry.getAsJsonObject(OBJ_PRICE).getAsJsonObject(ATTRIBUTES).get(AMOUNT).getAsDouble();
                // Category values
                idCategory  = entry.getAsJsonObject(OBJ_CATEGORY).getAsJsonObject(ATTRIBUTES).get(ID).getAsInt();
                categoryName    = entry.getAsJsonObject(OBJ_CATEGORY).getAsJsonObject(ATTRIBUTES).get(LABEL).getAsString();
                url         = entry.getAsJsonObject(OBJ_CATEGORY).getAsJsonObject(ATTRIBUTES).get(SCHEME).getAsString();

                category = new Category(idCategory, categoryName, url);

                app = new Application(id, name, title, summary, rights, link, date, image, currency, price, category);
                applications.add(app);
            }

        }

        return applications;

    }

}
