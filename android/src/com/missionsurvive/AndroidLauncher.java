package com.missionsurvive;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.missionsurvive.andviews.AndroidLook;
import com.missionsurvive.andviews.Look;
import com.missionsurvive.andviews.PopupBots;
import com.missionsurvive.scenarios.commands.Command;

public class AndroidLauncher extends AndroidApplication implements ActivityCallback {

	private Look look;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		look = new AndroidLook(this);
		initialize(new MSGame(this), config);
	}

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
