package com.example.rohithreddy.hkwikmint;

/**
 * Created by rohithreddy on 26/07/17.
 */

public class mapuser {
    private int id;
    private String username;
    private String outletname;
    private String phonenumber;
    private String lat;
    private String lan;

    public mapuser(int id, String outletname, String username, String phonenumber, String lat, String lan) {
        this.id = id;
        this.username = username;
        this.outletname = outletname;
        this.phonenumber = phonenumber;
        this.lat=lat;
        this.lan=lan;
    }

    public Double getLat() {
        double Lat = Double.parseDouble(lat);
        System.out.println("converted lat"+Lat);
        return Lat;
    }

    public Double getLan() {
        double Lan = Double.parseDouble(lan);
        System.out.println("converted lat"+Lan);
        return Lan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOutletname() {
        return outletname;
    }

    public void setOutletname(String outletname) {
        this.outletname = outletname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
