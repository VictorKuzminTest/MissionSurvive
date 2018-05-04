package com.missionsurvive;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.XML;
import com.missionsurvive.framework.impl.AndroidXML;
import com.missionsurvive.tests.screens.ButtonsTestScreen;
import com.missionsurvive.utils.Assets;

public class MSGame extends Game {
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 320;
	public static final int SCREEN_OFFSET_X = -240;
	public static final int SCREEN_OFFSET_Y = -160;

	SpriteBatch batch;
	XML xmlParser;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        xmlParser = new AndroidXML();
        Assets.setMapAssets(this);

		//setScreen(new TestScreen(this));
		setScreen(new ButtonsTestScreen(this));
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
}
