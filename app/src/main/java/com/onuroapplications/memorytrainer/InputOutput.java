package com.onuroapplications.memorytrainer;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class provides the option to save and read back data to and from the phone storage.
 */
public class InputOutput {

    public static final String INPUT_LIST = "savedArrayList";

    public static void write(ArrayList<String> arr, String type, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(type, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arr);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> read(String type, Context context) {
        ArrayList<String> arr = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(type);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arr = (ArrayList<String>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
