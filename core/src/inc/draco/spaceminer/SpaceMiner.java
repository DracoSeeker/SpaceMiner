package inc.draco.spaceminer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import inc.draco.spaceminer.screens.GameScreen;
import inc.draco.spaceminer.screens.SplashScreen;
import inc.draco.spaceminer.screens.WelcomeScreen;
import inc.draco.spaceminer.useful.Style;

public class SpaceMiner extends Game {

	public SpriteBatch batch;

	private ArrayMap<String, Screen> screenList;

	public AssetManager manager;

	public BitmapFont font;

	@Override
	public void create() {



		batch = new SpriteBatch();

		manager = new AssetManager();

		manager.load("logoOnly.png", Texture.class);
		manager.load("vortex.png", Texture.class);
		manager.finishLoading();

//		manager.load("Skins/default/skin/uiskin.atlas", TextureAtlas.class);
//		manager.load("Skins/default/skin/uiskin.json", Skin.class, new SkinLoader.SkinParameter("Skins/default/skin/uiskin.atlas"));
		manager.load("Env/stars.png", Texture.class);
		manager.load("Ships/SmallShip.png", Texture.class);
		manager.load("Atlas/SpaceMiner.atlas", TextureAtlas.class);
		manager.load("Env/starsGameTile.png", Texture.class);



		screenList = new ArrayMap<>();

		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font.getData().scale(3);


		setScreen(new SplashScreen(this));

	}

	public void init() {
		Style.createStyles();
		screenList.put("welcome", new WelcomeScreen(this));
		screenList.put("game", new GameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	public void changeScreen(String name) {
		setScreen(screenList.get(name));
	}

	@Override
	public void dispose() {
		for (ObjectMap.Entry<String, Screen> screenEntry : screenList) {
			screenEntry.value.dispose();
		}
		batch.dispose();
	}

}
