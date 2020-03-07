package com.dsd.machinerental;

/**
 * Created by Gemechu on 10/18/2017.
 */

public class House {
    private int id;
    private int userId;
    private String ptype;
    private String ctype;
    private String pbsize;
    private String pcsize;

    private String kitchen;
    private String parking;
    private String generators;
    private String servantQuarter;
    private String servantQuarterBath;
    private String fullbath;
    private String halfbath;
    private String watertank;
    private String servceType;
    private String noofroom;
    private String price;
    private String builtYear;
    private String image;
    private String mUname;
    private String mPhone;
    private String houseDesc;

    public House(int id, int useId, String ptype,
                 String ctype, String pbsize, String pcsize, String kitchen, String parking, String generators, String servantQuarter,
                 String servantQuarterBath, String fullbath, String halfbath, String watertank, String serviceType,
                 String noofroom, String price, String builtYear,
                 String image, String mUname,
                 String mPhone, String houseDesc) {
        this.id = id;
        this.userId = useId;
        this.ptype = ptype;
        this.ctype = ctype;
        this.kitchen =kitchen;
        this.parking = parking;
        this.generators = generators;
        this.servantQuarter = servantQuarter;
        this.servantQuarterBath = servantQuarterBath;
        this.fullbath = fullbath;
        this.halfbath = halfbath;
        this.watertank = watertank;
        this.servceType = serviceType;
        this.noofroom = noofroom;
        this.price = price;
        this.builtYear = builtYear;
        this.pbsize = pbsize;
        this.pcsize = pcsize;
        this.image = image;
        this.mUname = mUname;
        this.mPhone = mPhone;
        this.houseDesc = houseDesc;
    }

    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public String getKitchen() {
        return kitchen;
    }
    public String getParking() {
        return parking;
    }
    public String getGenerators() {
        return generators;
    }
    public String getServantQuarter() {
        return servantQuarter;
    }
    public String getServantQuarterBath() {
        return servantQuarterBath;
    }
    public String getFullbath() {
        return fullbath;
    }
    public String getHalfbath() {
        return halfbath;
    }
    public String getWatertank() {
        return watertank;
    }
    public String getServceType() {
        return servceType;
    }
    public String getCtype() {
        return ctype;
    }
    public String getPsize() {
        return pbsize;
    }

    public String getPcsize() {
        return pcsize;
    }

    public String getPtype() {
        return ptype;
    }
    public String getHouseDesc() {
        return houseDesc;
    }
    public String getNoofroom() {
        return noofroom;
    }
    public String getPrice() {
        return price;
    }
    public String getBuiltYear() {
        return builtYear;
    }
    public String getImage() {
        return image;
    }
    public String getmUname() {
        return mUname;
    }
    public String getmPhone() {
        return mPhone;
    }
}

