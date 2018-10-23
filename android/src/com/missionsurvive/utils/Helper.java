package com.missionsurvive.utils;

public class Helper {

    public static final String ITEM_ID_LIST = "ITEM_ID_LIST";
    public static final String SKU = "android.test.purchased"; //"com.missionsurvive.purchase";
    public static final int RESPONSE_CODE = 777;

    public String productId;
    //purchase
    public String storeName;
    //purchase details
    public String storeDescription;
    //3.50USD
    public String price;
    //"3500000" = price * 1000000
    public int priceAmountMicros;
    //USD
    public String currencyIsoCode;

    public Helper(){}

    public Helper(String productId){
        this.productId = productId;
    }

    public String getSku(){
        return productId;
    }
}
