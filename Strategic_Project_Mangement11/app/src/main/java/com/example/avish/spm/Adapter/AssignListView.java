package com.example.avish.spm.Adapter;

/**
 * Created by Avish on 13-05-2017.
 */

public class AssignListView {
    private String name;
    private int id;
    boolean checked;

    public AssignListView(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
