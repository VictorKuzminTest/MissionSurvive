package com.missionsurvive.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IAP {

    public static final int REQUEST_CODE_BUY = 777;

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;

    public static final int PURCHASE_STATUS_PURCHASED = 0;
    public static final int PURCHASE_STATUS_CANCELED = 1;
    public static final int PURCHASE_STATUS_REFUNDED = 2;

    private Context context;
    private IInAppBillingService inAppBillingService;

    public IAP(Context context, IInAppBillingService inAppBillingService){
        this.context = context;
        this.inAppBillingService = inAppBillingService;
    }

    public List<Helper> getInAppPurchases(String type, String... productIds) throws Exception{
        ArrayList<String> skuList = new ArrayList<>(Arrays.asList(productIds));
        Bundle query = new Bundle();
        query.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = inAppBillingService.getSkuDetails(3, context.getPackageName(), type, query);
        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAIL_LIST");
        List<Helper> result = new ArrayList<>();
        for(String responseItem : responseList){
            JSONObject jsonObject = new JSONObject((responseItem));
            Helper product = new Helper();
            product.productId = jsonObject.getString("productId");
            product.storeName = jsonObject.getString("title");
            product.storeDescription = jsonObject.getString("description");
            product.price = jsonObject.getString("price");
            product.priceAmountMicros = Integer.parseInt(jsonObject.getString("price_amount_micros"));
            product.currencyIsoCode = jsonObject.getString("price_currency_code");
            result.add(product);
        }
        return result;
    }


    public void purchaseProduct(Activity activity, Helper product){
        String sku = product.getSku();
        //here we can add arbitrary data. You could receive and use it later on.
        String developerPayload = "purchase";

        try{
            Bundle buyIntentBundle = inAppBillingService.getBuyIntent(
                    3, context.getPackageName(), sku, "inapp", developerPayload);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            try{
                activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                        REQUEST_CODE_BUY, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0), null);
            }
            catch(IntentSender.SendIntentException e){
                e.printStackTrace();
            }
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
    }


    public void readPurchase(String purchaseData) {
        try{
            JSONObject jsonObject = new JSONObject(purchaseData);
            //for testing purchases Id equals to null
            String orderId = jsonObject.optString("orderId");
            //"com.missionsurvive"
            String packageName = jsonObject.getString("packageName");
            //"com.missionsurvive.myproduct" - registered product
            String productId = jsonObject.getString("productId");
            //unix-timestamp of purchase time
            long purchaseTime = jsonObject.getLong("purchaseTime");
            //PURCHASE_STATUS_PURCHASED
            //PURCHASE_STATUS_CANCELED
            //PURCHASE_STATUS_REFUNDED
            int purchaseState = jsonObject.getInt("purchaseState");
            //"purchase"
            String developerPayload = jsonObject.optString("developerPayload");
            //purchase token. With this you can get purchase data on a server
            String purchaseToken = jsonObject.getString("purchaseToken");
            //Now I have to handle the purchase...
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * To get info about already made purchases.
     * @param type  type means weather it is subscription or one time purchase
     * @throws Exception
     */
    public void readMyPurchases(String type)throws Exception{
        String continuationToken = null;
        do{
            Bundle result = inAppBillingService.getPurchases(3, context.getPackageName(),
                    type, continuationToken);
            if(result.getInt("RESPONSE_CODE", -1) != 0){
                throw new Exception("Invalid response code");
            }
            List<String> responseList = result.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            for(String purchaseData : responseList){
                readPurchase(purchaseData);
            }
            continuationToken = result.getString("INAPP_CONTINUATION_TOKEN");
        }
        while(continuationToken != null);
    }


}
