package inc.draco.spaceminer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;
import inc.draco.spaceminer.models.Ship;

import inc.draco.spaceminer.info.Info;

import java.sql.Time;

public class GameScreen extends StandardScreen implements InputProcessor {

    SpaceMiner spaceMiner;

    private Viewport viewport;
    private Camera camera;

    private Viewport viewportGUI;
    private Camera cameraGUI;

    private Texture bkgd;

    private Ship ship;

    private World world;

    private Box2DDebugRenderer debug;

    public GameScreen(SpaceMiner spaceMiner) {
        Box2D.init();


        this.spaceMiner = spaceMiner;

        viewport = new FillViewport(Info.BKGD_WIDTH, Info.BKGD_HEIGHT);
        camera = viewport.getCamera();

        viewportGUI = new ExtendViewport(Info.GUI_WIDTH, Info.GUI_HEIGHT);
        cameraGUI = viewportGUI.getCamera();

        bkgd = spaceMiner.manager.get("Env/starsGameTile.png", Texture.class);
        bkgd.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        world = new World(new Vector2(0,-10), true);

        debug = new Box2DDebugRenderer();

        ship = new Ship(spaceMiner, viewport, world);

        BodyDef borderDef = new BodyDef();
        borderDef.type = BodyDef.BodyType.DynamicBody;
        borderDef.position.set(0,0);

        Body borderBody = world.createBody(borderDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(20);
        circleShape.setPosition(new Vector2(-20,0));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 5;

        borderBody.createFixture(fixtureDef);

        CircleShape circle2Shape = new CircleShape();
        circle2Shape.setRadius(5);
//        circle2Shape.setPosition(new Vector2(20,0));
        borderBody.createFixture(circle2Shape, 2);

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
            edgeFixDef.restitution = 0;
            edgeFixDef.friction = .0f;

            edge.createFixture(edgeFixDef);
        }

    }

    @Override
    public void scRender(float delta) {

        stepPhysics(delta);

        ship.update(delta);

        viewport.apply();
        spaceMiner.batch.setProjectionMatrix(camera.combined);
        spaceMiner.batch.begin();

        float camU = camera.position.x / Info.BKGD_WIDTH;
        float camV = camera.position.y / Info.BKGD_HEIGHT;

//        spaceMiner.batch.draw(bkgd, 0, 0, BKGD_WIDTH, BKGD_HEIGHT);
        spaceMiner.batch.draw(bkgd, camera.position.x - Info.BKGD_WIDTH / 2f,  camera.position.y - Info.BKGD_HEIGHT / 2f, Info.BKGD_WIDTH, Info.BKGD_HEIGHT,
                (camU) - .5f,  (camV) - .5f,
                (camU) + .5f,  (camV) + .5f);

        ship.draw(spaceMiner.batch);

        spaceMiner.batch.end();

        debug.render(world, camera.combined);


    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewportGUI.update(width, height);
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
        return false;
    }
}
