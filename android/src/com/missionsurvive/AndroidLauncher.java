package com.missionsurvive;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.vending.billing.IInAppBillingService;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.missionsurvive.andviews.AndroidLook;
import com.missionsurvive.andviews.Look;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.utils.IAP;
import com.missionsurvive.utils.Helper;
import com.missionsurvive.utils.IabHelper;
import com.missionsurvive.utils.IabResult;
import com.missionsurvive.utils.Inventory;
import com.missionsurvive.utils.PTask;
import com.missionsurvive.utils.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidLauncher extends AndroidApplication implements ActivityCallback {

	public String KEY_BASE64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjb/icdtqACdYxEFEa4tZK/EQmlj8yuZPXa+TFClrZ83vSynzhsbd57/ab73lidn37/WX9JESLPIWEDHqocnxg3sq7uPXpQMfXVNS1en1sxIPMRcSIgx46Gpp6EpMlyceT3J2vN7PFDLeNvkNMPTE2T+0NdK7jhB6jeh04LDFUsEs++lCRRZvwFXD/bArD9PlXolFMlJsGjtTG+VHFsc4HPw2JDP3iKmurOU4GjISUVgGDEK5/UHdlgwdlcBuJrX/F8aFyhUSGvoLXVA4IDevQEM1xOn6IbtTX3eQWv9I+CIOd/qAOC7uxQ979Dn42iXfEH0KI7iV9FIDLWdqDl/j1QIDAQAB";

	private String[] productIds = new String[]{Helper.SKU};
;	private Look look;
	private IInAppBillingService inAppBillingService;
	private String appPackageName;

    private static final String TAG = "com.missionsurvive.bill";
    private IabHelper mHelper;

	/*private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			inAppBillingService = IInAppBillingService.Stub.asInterface(service);
			PTask pTask = new PTask(inAppBillingService, appPackageName);
			pTask.execute();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			inAppBillingService = null;
		}
	};*/

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
    	@Override
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase){
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(Helper.SKU)) {
                //consumeItem();
                setIntoSharedPrefs("purchase", "purchase");
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (inventory.hasPurchase(Helper.SKU)) {
                setIntoSharedPrefs("purchase", "purchase");
            }
            else{
                initiatePurchase();
            }
        }
    };


    public void loadPurchase(){
		if(mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
			try{
				//INSTEAD OF THIS WE HAVE TO USE THE METHOD WITH
				//PARAMETERS FROM THE EXAMPLE. OTHERWISE -
				//WE GET NULL POINTER EXCEPTION IN CALLBACK!!!!!!!!!!!
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
			catch (IabHelper.IabAsyncInProgressException e){
				e.printStackTrace();
			}
		}
    }

    public void initiatePurchase(){
        try{
            mHelper.launchPurchaseFlow(this, Helper.SKU, 10001,
                    mPurchaseFinishedListener, "mypurchasetoken");
        }
        catch(IabHelper.IabAsyncInProgressException e){
            e.printStackTrace();
        }
    }


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		look = new AndroidLook(this);

		/*appPackageName = getPackageName();
		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);*/

        String base64EncodedPublicKey = KEY_BASE64;
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
									   public void onIabSetupFinished(IabResult result)
									   {
										   if (!result.isSuccess()) {
											   Log.d(TAG, "In-app Billing setup failed: " +
													   result);
											   //This is the In-app Billing Reference (IAB Version 3), so the error means that the IAB v3 is not installed on the device.
											   //Actually this means that the user has a google account,
											   //and probably also an in-app billing service, but it doesnt has
											   // the last version.
											   //WE CAN INSTEAD OF LOG.D SHOW MESSAGE
											   //TOAST WITH RESULT IN IT.
										   } else {
											   Log.d(TAG, "In-app Billing is set up OK");
										   }
									   }
								   });

		initialize(new MSGame(this), config);
	}


	/*@Override
	protected void onDestroy() {
		super.onDestroy();
		if(serviceConnection != null){
			unbindService(serviceConnection);
		}
	}*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }

	@Override
	public void purchaseFullVersion(){
		//-purchaseProduct(Helper.SKU);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadPurchase();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent data){
		if (!mHelper.handleActivityResult(requestCode,
				resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/*public void purchaseProduct(String sku){
		//here we can add arbitrary data. You could receive and use it later on.
		String developerPayload = "purchase";

		try{
			Bundle buyIntentBundle = inAppBillingService.getBuyIntent(
					3, getPackageName(), sku, "inapp", developerPayload);
			PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
			try{
				startIntentSenderForResult(pendingIntent.getIntentSender(),
						Helper.RESPONSE_CODE, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
						Integer.valueOf(0), null);

			}
			catch(IntentSender.SendIntentException e){
				e.printStackTrace();
			}
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
	}*/

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == IAP.REQUEST_CODE_BUY){
			int responseCode = data.getIntExtra("RESPONSE_CODE", -1);
			if(responseCode == IAP.BILLING_RESPONSE_RESULT_OK){
				String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
				String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
				//you can check digital signature
				//if(Sec.verifyPurchase(KEY_BASE64, purchaseData, dataSignature)){
					iap.readPurchase(purchaseData);
				//}
			}
			else{
				//handle some answer...
			}
		}
	}*/

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Helper.RESPONSE_CODE){
			int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
			String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
			if(resultCode == RESULT_OK){
				try{
					JSONObject purchaseJSONObject = new JSONObject(purchaseData);
					String sku = purchaseJSONObject.getString("productId");
					String developerPayLoad = purchaseJSONObject.getString("developerPayload");
					String purchaseToken = purchaseJSONObject.getString("purchaseToken");

					for(int i = 0; i < productIds.length; i++){
						if (productIds[i].equals(sku)) {
							setIntoSharedPrefs("purchase", "purchase");
						}
					}
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
		}
	}*/


	public Look getLook(){
		return look;
	}

	@Override
	public String getSharedPrefs(String key) {
		String value = "";
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(key, Context.MODE_PRIVATE);
		if(sharedPreferences != null){
			//value is a value to return if this preference does not exist:
			value = sharedPreferences.getString(key, value);
		}
		return value;
	}

	@Override
	public void setIntoSharedPrefs(String key, String value) {
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(key, Context.MODE_PRIVATE);

		if(sharedPreferences != null){
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	@Override
	public void setCommand(Command command, String popupName) {
		if(popupName.equalsIgnoreCase("popup_new_bot")){
			getLook().getPopups().get(AndroidLook.POPUP_NEW_BOT).setCommand(command);
		}
		else if(popupName.equalsIgnoreCase("load_map")){
			getLook().getPopups().get(AndroidLook.POPUP_LOAD).setCommand(command);
		}
		else if(popupName.equalsIgnoreCase("new_map")){
			getLook().getPopups().get(AndroidLook.POPUP_NEW_MAP).setCommand(command);
		}
		else if(popupName.equalsIgnoreCase("save_map")){
			getLook().getPopups().get(AndroidLook.POPUP_SAVE).setCommand(command);
		}
	}

	@Override
	public void showPopup(int dialogId) {
		switch (dialogId){
			case AndroidLook.POPUP_SAVE: this.runOnUiThread(new Runnable(){
				public void run(){
					if(!isFinishing()){
						getLook().getPopups().get(AndroidLook.POPUP_SAVE).show();
					}
				}
			});
				break;
			case AndroidLook.POPUP_LOAD: this.runOnUiThread(new Runnable(){
				public void run(){
					if(!isFinishing()){
						getLook().getPopups().get(AndroidLook.POPUP_LOAD).show();
					}
				}
			});
				break;
			case AndroidLook.POPUP_NEW_MAP: this.runOnUiThread(new Runnable(){
				public void run(){
					if(!isFinishing()){
						getLook().getPopups().get(AndroidLook.POPUP_NEW_MAP).show();
					}
				}
			});
				break;
			case AndroidLook.POPUP_NEW_BOT: this.runOnUiThread(new Runnable(){
				public void run(){
					if(!isFinishing()){
						getLook().getPopups().get(AndroidLook.POPUP_NEW_BOT).show();
					}
				}
			});
				break;
			case AndroidLook.POPUP_BUY: this.runOnUiThread(new Runnable(){
				public void run(){
					if(!isFinishing()){
						getLook().getPopups().get(AndroidLook.POPUP_BUY).show();
					}
				}
			});
				break;
		}
	}
}
