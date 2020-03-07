package com.dsd.machinerental;

public class Machine {
    private int machineId;
    private int userId;
    private String mUname;
    private String mPhone;
    private String machineType;
    private String machineModel;
    private String yearManufactured;
    private String withorWithoutDrivers;
    private String price;
    private String machineDescription;
    private String image;


    public Machine(int machineId, int userId, String mUname, String mPhone, String machineType, String image,String machineModel, String yearManufactured, String withorWithoutDrivers, String price, String machineDescription) {
        this.machineId = machineId;
        this.userId = userId;
        this.mUname = mUname;
        this.mPhone = mPhone;
        this.machineType = machineType;
        this.machineModel = machineModel;
        this.yearManufactured = yearManufactured;
        this.withorWithoutDrivers = withorWithoutDrivers;
        this.price = price;
        this.machineDescription = machineDescription;
        this.image = image;
    }

    public int getMachineId() {
        return machineId;
    }

    public int getUserId() {
        return userId;
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

    public String getMachineType() {
        return machineType;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public String getYearManufactured() {
        return yearManufactured;
    }

    public String getWithorWithoutDrivers() {
        return withorWithoutDrivers;
    }

    public String getPrice() {
        return price;
    }

    public String getMachineDescription() {
        return machineDescription;
    }
}
