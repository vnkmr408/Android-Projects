package com.example.avish.spm.Adapter;

/**
 * Created by Avish on 10-05-2017.
 */

public class EmployeeRow {
    private int memberid;
    private String fname, lname, address, mothername, designation;

    public EmployeeRow(int memberid, String fname, String lname, String address, String mothername, String designation) {
        this.memberid = memberid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.mothername = mothername;
        this.designation = designation;
    }

    public int getMemberid() {
        return memberid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getMothername() {
        return mothername;
    }

    public String getDesignation() {
        return designation;
    }
}
