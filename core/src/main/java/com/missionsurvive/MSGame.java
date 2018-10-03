package com.missionsurvive;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.XML;
import com.missionsurvive.framework.impl.AndroidXML;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.ScreenFactory;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Controls;
import com.missionsurvive.utils.Sounds;

public class MSGame extends Game {
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 320;
	public static final int SCREEN_OFFSET_X = -240;
	public static final int SCREEN_OFFSET_Y = -160;

	SpriteBatch batch;
	XML xmlParser;
	private ActivityCallback activityCallback;
	private ScreenFactory screenFactory;

	public MSGame(ActivityCallback activityCallback){
		super();
		this.activityCallback = activityCallback;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        xmlParser = new AndroidXML();
        Assets.setMapAssets(this);
		Assets.setGame(this);
		Controls.setControls(this);

		PlayScript playScript = new PlayScript();
		screenFactory = new ScreenFactory(this, playScript);
		Sounds.loadSounds();

		//test screens:
		//setScreen(new TestScreen(this));
		//setScreen(new ButtonsTestScreen(this));

		//start editor screen:
        //setScreen(screenFactory.newScreen("LoadingLevelScreen", null, "EditorScreen"));

		//start game screen:
		setScreen(screenFactory.newScreen("GameMenuScreen",null, null));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}

	public SpriteBatch getSpriteBatch(){
		return batch;
	}

	public XML getXMLParser(){
		return xmlParser;
	}

	public ActivityCallback getActivityCallback(){
		return activityCallback;
	}

	public ScreenFactory getScreenFactory(){
		return screenFactory;
	}
}
