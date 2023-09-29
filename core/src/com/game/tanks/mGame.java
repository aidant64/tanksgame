package com.game.tanks;

import Screens.GameScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import Screens.GameScreen;
import Screens.World;
import com.badlogic.gdx.*;

public class mGame extends Game {
	GameScreen gameScreen;

	@Override
	public void create() {
		gameScreen=new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
	}

	@Override
	public void render() {
		gameScreen.render(0.1f);
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
}
