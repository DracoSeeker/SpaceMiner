package inc.draco.spaceminer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;
import inc.draco.spaceminer.useful.Style;

public class WelcomeScreen extends StandardScreen{

    private SpaceMiner spaceMiner;

    private Camera textCamera;
    private Viewport textViewport;

    private Viewport bkgdViewport;

    private Sprite bkgd;

    private Stage stage;
        private Table table;
            private Label title;
            private Label instructions;


    public WelcomeScreen(SpaceMiner spaceMiner) {
        this.spaceMiner = spaceMiner;

        textViewport = new FitViewport(1024, 1024);
        textCamera = textViewport.getCamera();

        stage = new Stage(textViewport, spaceMiner.batch);

            table = new Table();
            table.setFillParent(true);
            stage.addActor(table);

                title = new Label("Space Miner", Style.defaultSkin, "title");

                instructions = new Label("CLick Anywhere to Begin", Style.defaultSkin, "paragraph");

            table.add(title);
            table.row();
            table.add(instructions);


        bkgdViewport = new ScreenViewport();

        bkgd = new Sprite(spaceMiner.manager.get("Env/stars.png", Texture.class));
        bkgd.setCenter(0,0);

    }

    @Override
    public void scRender(float delta) {

        if (Gdx.input.isTouched()) {
            spaceMiner.changeScreen("game");
        }

        bkgdViewport.apply(true);
        spaceMiner.batch.setProjectionMatrix(bkgdViewport.getCamera().combined);
        spaceMiner.batch.begin();
        bkgd.draw(spaceMiner.batch);
        spaceMiner.batch.end();

        textViewport.apply();
        spaceMiner.batch.setProjectionMatrix(textCamera.combined);
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        bkgdViewport.update(width, height);
        textViewport.update(width, height);
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
