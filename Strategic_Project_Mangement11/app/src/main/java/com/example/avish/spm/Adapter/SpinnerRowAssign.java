package com.example.avish.spm.Adapter;

/**
 * Created by Avish on 12-05-2017.
 */

public class SpinnerRowAssign {
    private int id;
    private String name;

    public SpinnerRowAssign(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
