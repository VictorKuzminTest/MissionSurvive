package com.missionsurvive.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

import java.util.ArrayList;

public class PTask extends AsyncTask<Void, Void, Bundle> {

    private IInAppBillingService inAppBillingService;
    private String appPackageName;

    public PTask(IInAppBillingService inAppBillingService, String appPackageName){
        this.inAppBillingService = inAppBillingService;
        this.appPackageName = appPackageName;
    }

    @Override
    protected Bundle doInBackground(Void... voids) {
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add(Helper.SKU);
        Bundle query = new Bundle();
        query.putStringArrayList(Helper.ITEM_ID_LIST, skuList);
        Bundle skuDetails = null;
        try{
            skuDetails = inAppBillingService.getSkuDetails(3, appPackageName,
                    "inapp", query);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        return skuDetails;
    }
}
