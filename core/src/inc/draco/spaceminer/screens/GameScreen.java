package inc.draco.spaceminer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;
import inc.draco.spaceminer.models.Asteroid;
import inc.draco.spaceminer.models.Ship;

import inc.draco.spaceminer.info.Info;
import inc.draco.spaceminer.useful.Style;

import java.sql.Time;

public class GameScreen extends StandardScreen implements InputProcessor {

    SpaceMiner spaceMiner;

    private float zoom = 1;
    private float zoomPerScroll = .1f;

    private Viewport viewport;
    private Camera camera;

    private Viewport viewportGUI;
    private Camera cameraGUI;

    private Stage stage;
        private Table GUITable;
            private Table statsTable;
                private Label healthLb; private Label healthAmt;
                private Label coinLb; private Label coinAmt;
                private Label speedLb; private Label speecAmt;


    private Texture bkgd;

    private Ship ship;

    private World world;

    private Array<Asteroid> roids;

    private Box2DDebugRenderer debug;

    public GameScreen(SpaceMiner spaceMiner) {
        Box2D.init();


        this.spaceMiner = spaceMiner;

        viewport = new FillViewport(Info.BKGD_WIDTH, Info.BKGD_HEIGHT);
        camera = viewport.getCamera();

        viewportGUI = new ScreenViewport();
        cameraGUI = viewportGUI.getCamera();

        stage = new Stage(viewportGUI, spaceMiner.batch);
        stage.setDebugAll(true);

            GUITable = new Table();
            GUITable.setFillParent(true);
            GUITable.align(Align.topLeft);
            stage.addActor(GUITable);

                statsTable = new Table();
                statsTable.setBackground(new NinePatchDrawable(new NinePatch(spaceMiner.manager.get("StatsBKGD.png", Texture.class), 10, 10, 10, 10)));
                statsTable.setWidth(200);
                statsTable.setHeight(200);
                GUITable.add(statsTable);

                    healthLb = new Label("FPS ", Style.defaultSkin, "stat");
                    healthAmt = new Label("10", Style.defaultSkin,"stat");



                statsTable.add(healthLb);
                statsTable.add(healthAmt);

        bkgd = spaceMiner.manager.get("Env/starsGameTile.png", Texture.class);
        bkgd.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        world = new World(new Vector2(0,0), true);

        debug = new Box2DDebugRenderer();

        ship = new Ship(spaceMiner, viewport, world);

//        BodyDef borderDef = new BodyDef();
//        borderDef.type = BodyDef.BodyType.DynamicBody;
//        borderDef.position.set(0,0);
//
//        Body borderBody = world.createBody(borderDef);
//
//        CircleShape circleShape = new CircleShape();
//        circleShape.setRadius(20);
//        circleShape.setPosition(new Vector2(-20,0));
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = circleShape;
//        fixtureDef.density = 5;
//
//        borderBody.createFixture(fixtureDef);
//
//        CircleShape circle2Shape = new CircleShape();
//        circle2Shape.setRadius(5);
////        circle2Shape.setPosition(new Vector2(20,0));
//        borderBody.createFixture(circle2Shape, 2);

        roids = new Array<>();
//        roids.add(new Asteroid(spaceMiner, world, 0, 0));

        for (int i = 0; i < 1000; i++) {
            roids.add(new Asteroid(spaceMiner, world, MathUtils.random(-Info.WORLD_WIDTH / 2f, Info.WORLD_WIDTH / 2f),
                    MathUtils.random(-Info.WORLD_HEIGHT / 2f, Info.WORLD_HEIGHT / 2f)));
        }

        createEdge();

        Gdx.input.setInputProcessor(this);
    }

    float accumulator = 0;

    private void stepPhysics(float delta) {
        accumulator += delta;
        while (accumulator >= Info.TIME_STEP) {
            world.step(Info.TIME_STEP, Info.VELOCITY_ITERATIONS, Info.POSITION_ITERATIONS);
            accumulator -= Info.TIME_STEP;
        }
    }

    private void createEdge() {

        BodyDef edgeBodyDef = new BodyDef();
        edgeBodyDef.position.set(0, 0);
        edgeBodyDef.type = BodyDef.BodyType.StaticBody;

        Body edge = world.createBody(edgeBodyDef);

        float[][] vertices = {
                {-1, 1, 1, 1},
                {1, 1, 1, -1},
                {1, -1, -1, -1},
                {-1, -1, -1, 1}
        };

        for (int i = 0; i < vertices.length; i++) {

            EdgeShape edgeShape = new EdgeShape();
            edgeShape.set(vertices[i][0] * Info.WORLD_WIDTH / 2, vertices[i][1] * Info.WORLD_HEIGHT / 2,
                    vertices[i][2] * Info.WORLD_WIDTH / 2,vertices[i][3] * Info.WORLD_HEIGHT / 2);

            FixtureDef edgeFixDef = new FixtureDef();
            edgeFixDef.shape = edgeShape;
            edgeFixDef.density = 1000;
            edgeFixDef.restitution = 1;
            edgeFixDef.friction = .0f;

            edge.createFixture(edgeFixDef);
        }

    }

    @Override
    public void scRender(float delta) {

        viewport.setWorldSize(Info.BKGD_WIDTH * zoom, Info.BKGD_HEIGHT * zoom);

        stepPhysics(delta);

        ship.update(delta);


        healthAmt.setText(Gdx.graphics.getFramesPerSecond());

        viewport.apply();
        spaceMiner.batch.setProjectionMatrix(camera.combined);
        spaceMiner.batch.begin();

        float camU = camera.position.x / Info.BKGD_WIDTH;
        float camV = camera.position.y / Info.BKGD_HEIGHT;

//        spaceMiner.batch.draw(bkgd, 0, 0, BKGD_WIDTH, BKGD_HEIGHT);
        spaceMiner.batch.draw(bkgd, camera.position.x - viewport.getWorldWidth() / 2f,  camera.position.y - viewport.getWorldHeight() / 2f, viewport.getWorldWidth(), viewport.getWorldHeight(),
                (camU) - (viewport.getWorldWidth() / Info.BKGD_WIDTH) / 2,  (camV) - (viewport.getWorldHeight() / Info.BKGD_WIDTH) / 2,
                (camU) + (viewport.getWorldWidth() / Info.BKGD_WIDTH) / 2,  (camV) + (viewport.getWorldHeight() / Info.BKGD_WIDTH) / 2);

        for (Asteroid roid : roids) {
            roid.update(delta, spaceMiner.batch);
        }

        ship.draw(spaceMiner.batch);

        spaceMiner.batch.end();

        debug.render(world, camera.combined);

        viewportGUI.apply();
        spaceMiner.batch.setProjectionMatrix(cameraGUI.combined);
        stage.draw();


    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewportGUI.update(width, height, true);
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

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        zoom += zoomPerScroll * amount;
        if (zoom < 1) zoom = 1;
        if(zoom > 10) zoom = 10;
        System.out.println("zoom = " + zoom);
        return false;
    }
}
