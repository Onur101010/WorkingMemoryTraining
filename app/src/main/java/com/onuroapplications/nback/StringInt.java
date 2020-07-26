package com.onuroapplications.nback;

/**
 * This class provides the possibility to create objects of
 * (String string, Int int) tuples which can be used to store
 * for example as array of tuple objects.
 */
public class StringInt {
    private String s;
    private int i;

    public StringInt(String s, int i) {
        this.s = s;
        this.i = i;
    }

    public int getInt(){
        return i;
    }

    public String getString() {
        return s;
    }
}
