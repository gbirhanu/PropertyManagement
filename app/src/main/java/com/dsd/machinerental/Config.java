package com.dsd.machinerental;

/**
 * Created by user on 6/20/2017.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://michutaxi.com/android_connect/addHouse.php";
    public static final String URL_UPDATE="http://michutaxi.com/android_connect/updateHome.php";
    public static final String URL_DEACT="http://michutaxi.com/android_connect/deactivateHouse.php?id=";
    public static final String URL_GET_ALL = "http://michutaxi.com/android_connect/getAllHouse.php";
    public static final String URL_GET_ONE = "http://michutaxi.com/android_connect/getOnHouse.php?housePrice=";
    public static final String URL_GET_HOUSE_BYID = "http://michutaxi.com/android_connect/getHouseById.php?id=";
    public static final String URL_GET_HOUSE_RENTORSALE = "http://michutaxi.com/android_connect/getHouseForSaleorRent.php?houseisf=";
    public static final String URL_ADD_MORE_PICTURE = "http://michutaxi.com/android_connect/addMorePhoto.php";
    public static final String URL_ADD_USER="http://michutaxi.com/android_connect/userRegistration.php";
    public static final String URL_LOGOUT_USER="http://michutaxi.com/android_connect/logoutUser.php";
    public static final String URL_ACTIVATE_HOUSE="http://michutaxi.com/android_connect/activateHouse.php?id=";

    public static final String URL_GET_ACTIVATE_HOUSE="http://michutaxi.com/android_connect/getInactiveHouse.php?id=";
    public static final String URL_LOGIN_USER="http://michutaxi.com/android_connect/login.php";
    public static final String URL_UPLOAD="http://michutaxi.com/android_connect/addMorePhoto.php";
    public static final String URL_GET_UNAME_UPHONE="http://michutaxi.com/android_connect/getPhoneandUname.php?id=";

    public static final String  URL_GET_ALL_PHOTO="http://michutaxi.com/android_connect/getAllPhoto.php?id=";
    public static final String  URL_ACTIVATE="http://michutaxi.com/android_connect/activateUser.php";
    public static final String  URL_GET_USER="http://michutaxi.com/android_connect/getUserInfo.php";
    public static final String  URL_GET_NUMBER_HOUSE="http://michutaxi.com/android_connect/numberrentorsale.php?houseisf=";
    public static final String  URL_GET_HOUSE_BYUID="http://michutaxi.com/android_connect/getHouseByUId.php?id=";
    public static final String URL_UPDATE_HOUSE = "http://10.140.38.14/android_connect/updateHouse.php";
    public static final String URL_DELETE_HOUSE = "http://10.140.38.14/android_connect/deleteHouse.php?id=";
    //Machinery Configuration files
    public static final String URL_ADD_MACHINERY="http://michutaxi.com/android_connect/addMachinery.php";
    public static final String URL_GET_NUMBER_MACHINERY ="http://michutaxi.com/android_connect/getNumberOfMachinery.php";

    public static final String URL_GET_ALL_MACHINERY = "http://michutaxi.com/android_connect/getAllMachine.php";
    public static final String URL_ADD_MORE_MACHINERY_PICTURE = "http://michutaxi.com/android_connect/addMoreMachineryPhoto.php";

    public static final String  URL_GET_MACHINE_BYUID="http://michutaxi.com/android_connect/getMachineryByUid.php?id=";
    public static final String URL_UPDATE_MACHINERY="http://michutaxi.com/android_connect/updateMachinery.php";
    public static final String URL_GET_ACTIVATE_MACHINERY="http://michutaxi.com/android_connect/getInactiveMachinery.php?id=";
    public static final String URL_DEACT_MACHINERY="http://michutaxi.com/android_connect/deactivateMachinery.php?id=";
    public static final String URL_ACTIVATE_MACHINERY="http://michutaxi.com/android_connect/activateMachinery.php?id=";
    public static final String  URL_GET_ALL_MACHINERY_PHOTO="http://michutaxi.com/android_connect/getAllMachineryPhoto.php?id=";




    
    //Machinery Key Parameters
    public static final String KEY_MACHINE_TYPE = "machineType";
    public static final String KEY_MACHINE_ID = "machineId";

    public static final String KEY_MACHINE_MODEL = "machineModel";
    public static final String KEY_WITH_OR_WITHOUT_DRIVER = "driverStatus";

    //User Information Key
    public static final String KEY_USER_ID="userId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PASSWORD = "userPassword";
    public static final String  KEY_LOGGED_IN ="isLoggedIn";
    public static final String KEY_PHONE = "userPhone";
    public static final String KEY_STATUS = "userStatus";
    //Keys that will be used to send the request to php scripts
    public static final String KEY_HOUSE_ID = "houseId";
    public static final String KEY_HOUSE_TYPE = "houseType";
    public static final String KEY_HOUSE_CONST = "houseConstType";
    public static final String KEY_HOUSE_FULL_BATH = "fullBath";
    public static final String KEY_HOUSE_HALF_BATH = "halfBath";

    public static final String KEY_HOUSE_MODERN_KITCHEN="modernKitchen";
    public static final String KEY_HOUSE_PARKING="parkingSpace";
    public static final String KEY_HOUSE_WATER_TANK="waterTank";
    public static final String KEY_HOUSE_GENERATOR="generatorAvailable";
    public static final String KEY_HOUSE_DESCRIPTION="houseDescription";


    public static final String KEY_HOUSE_PRICE = "housePrice";
    public static final String KEY_HOUSE_NOOFROOM = "noofRoom";
    public static final String KEY_HOUSE_SERVTYPE="serviceType";
    public static final String KEY_HOUSE_BUILT_YEAR = "builtYear";

    public static final String KEY_HOUSE_SERVANT_QUARTER="servantQuarter";
    public static final String KEY_HOUSE_SERVANT_QUARTER_BATH="servantQuarterBath";
    public static final String KEY_HOUSE_COMPOUND_SIZE="CompoundSquareMeter";

    public static final String KEY_HOUSE_BUILDING_SIZE="buildingSquareMeter";
    public static final String KEY_HOUSE_LATITUDE="houseLatitude";
    public static final String KEY_HOUSE_LONGITUDE="houseLongitude";

    //JSON Tags
    public static final String TAG_USER_ID="userId";
    public static final String TAG_USER_NAME = "userName";
    public static final String TAG_BUILDING_SIZE="buildingSquareMeter";
    public static final String TAG_PHONE = "userPhone";
    public static final String TAG_TOTAL_NUMBER = "totalNumber";
    //
    public static final String TAG_KITCHEN="modernKitchen";
    public static final String TAG_PARKING="parkingSpace";
    public static final String TAG_GENERATOR="generatorAvailable";
    public static final String TAG_DESCRIPTION="houseDescription";
    public static final String TAG_QUARTER="servantQuarter";
    public static final String TAG_QUARTER_BATH="servantQuarterBath";

    public static final String TAG_COMPOUND_SIZE="CompoundSquareMeter";

    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "houseId";
    public static final String TAG_STATUS = "userStatus";
    public static final String  TAG_LOGGED_IN ="isLoggedIn";

    public static final String TAG_MACHINE_TYPE = "machineType";
    public static final String TAG_MACHINE_MODEL = "machineModel";
    public static final String TAG_WITH_OR_WITHOUT_DRIVER = "driverStatus";

    public static final String TAG_TYPE = "houseType";
    public static final String TAG_CONSTYPE = "houseConstType";
    public static final String TAG_FULL_BATH = "fullBath";
    public static final String TAG_HALF_BATH = "halfBath";
    public static final String TAG_WATER_TANK="waterTank";
    public static final String TAG_PRICE = "housePrice";
    public static final String TAG_NOOFROOM = "noofRoom";
    public static final String TAG_SERVTYPE="serviceType";
    public static final String TAG_BUILT_YEAR="builtYear";
    public static final String TAG_PHOTO="housePhoto";
    public static final String TAG_LATITUDE="houseLatitude";
    public static final String TAG_LONGITUDE="houseLongitude";
    //employee id to pass with intent
    public static final String HOUSE_ID = "HOUSE_id";
}