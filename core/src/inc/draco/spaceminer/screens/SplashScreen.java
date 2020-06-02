package inc.draco.spaceminer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;

public class SplashScreen extends StandardScreen {

    private SpaceMiner spaceMiner;

    private Camera camera;
    private Viewport viewport;
    private Sprite logo;
    private Sprite vortex;

    float loadingX = 0;

    float timeOut = 0;
    long startTime;

    public SplashScreen(SpaceMiner spaceMiner) {
        this.spaceMiner = spaceMiner;

        viewport = new FitViewport(1024,1024);
        camera = viewport.getCamera();
        camera.update();

        logo = new Sprite(spaceMiner.manager.get("logoOnly.png", Texture.class));
        logo.setOriginCenter();
        logo.setCenterX(0);
        logo.setCenterY(0);

        vortex = new Sprite(spaceMiner.manager.get("vortex.png", Texture.class));
        vortex.setOriginCenter();
        vortex.setCenter(0,0);

        spaceMiner.batch.enableBlending();
    }

    @Override
    public void scRender(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        vortex.rotate(-20 * delta);

        camera.update();
        spaceMiner.batch.setProjectionMatrix(camera.combined);
        spaceMiner.batch.begin();
        vortex.draw(spaceMiner.batch);
        logo.draw(spaceMiner.batch);
        loadingX = spaceMiner.font.draw(spaceMiner.batch, "Loading...", loadingX, 400).width / -2f;
        spaceMiner.batch.end();

        if (spaceMiner.manager.update() && TimeUtils.timeSinceMillis(startTime) > timeOut) {
            spaceMiner.init();
            spaceMiner.changeScreen("game");
        }
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
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
