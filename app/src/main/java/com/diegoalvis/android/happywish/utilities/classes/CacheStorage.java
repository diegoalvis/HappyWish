package com.diegoalvis.android.happywish.utilities.classes;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by diegoalvis on 1/26/17.
 */

public abstract class CacheStorage {

    // function for saving object in cache
    public static void writeObject(Context context, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(context.getCacheDir().getName(), Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    // function for reading object of cache
    public static Object readObject(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(context.getCacheDir().getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

}

