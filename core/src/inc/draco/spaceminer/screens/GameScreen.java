package inc.draco.spaceminer.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;

public class GameScreen extends StandardScreen{

    private final float WIDTH = 1024;
    private final float HEIGHT = WIDTH;

    SpaceMiner spaceMiner;

    private Viewport viewport;
    private Camera camera;

    private Texture bkgd;

    public GameScreen(SpaceMiner spaceMiner) {
        this.spaceMiner = spaceMiner;

        viewport = new FillViewport(WIDTH, HEIGHT);
        camera = viewport.getCamera();
        System.out.println(camera.position);

        bkgd = spaceMiner.manager.get("Env/starsGameTile.png", Texture.class);
        bkgd.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

    }

    @Override
    public void scRender(float delta) {

        viewport.apply();
        spaceMiner.batch.setProjectionMatrix(camera.combined);
        spaceMiner.batch.begin();

        spaceMiner.batch.draw(bkgd, 0, 0, (int)(camera.position.x), (int)(camera.position.y), WIDTH, HEIGHT);

        spaceMiner.batch.end();

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
