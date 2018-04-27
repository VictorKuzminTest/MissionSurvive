package com.missionsurvive;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.XML;
import com.missionsurvive.framework.impl.AndroidXML;
import com.missionsurvive.tests.ButtonsTestScreen;
import com.missionsurvive.tests.TestScreen;
import com.missionsurvive.utils.Assets;

public class MSGame extends Game {
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
