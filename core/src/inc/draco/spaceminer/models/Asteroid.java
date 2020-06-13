package inc.draco.spaceminer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import inc.draco.spaceminer.SpaceMiner;

public class Asteroid extends Sprite {

    private SpaceMiner spaceMiner;
    private Texture texture;

    private Body body;

    private float turn;

    private float size;

    private float density;

    private float speed;
    private float ang;

    public Asteroid(SpaceMiner spaceMiner, World world, float x, float y) {
        super(spaceMiner.manager.get("Atlas/SpaceMiner.atlas", TextureAtlas.class).findRegions("rock").get(1));
        this.spaceMiner = spaceMiner;

        TextureAtlas atlas = spaceMiner.manager.get("Atlas/SpaceMiner.atlas", TextureAtlas.class);

//        String name = String.format("Env/shot_%1$04d", (int));
        texture = atlas.findRegions("rock").get(MathUtils.random(2, 9)).getTexture();

        size = MathUtils.random(1f, 2f);
        turn = MathUtils.random(-1f, 1f);
        density = MathUtils.random(50f, 80f);
        speed = MathUtils.random(.1f,.25f);
        ang = MathUtils.random(0f,360f);

        setSize(size, size);

        setCenter(x, y);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.angularVelocity = turn;
        def.angularDamping = .2f;
        def.linearDamping = 0f;
        def.linearVelocity.set(1,1);
        def.linearVelocity.setLength(speed);
        def.linearVelocity.setAngle(ang);

        body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(.85f * size / 2);

        FixtureDef fixdef = new FixtureDef();
        fixdef.density = density * size;
        fixdef.restitution = 1;
        fixdef.shape = shape;

        body.createFixture(fixdef);
    }

    public void update(float delta, SpriteBatch batch) {
        setCenter(body.getPosition().x, body.getPosition().y);
//        setPosition(body.getPosition().x, body.getPosition().y);
//        System.out.println(body.getPosition().x);
        setOriginCenter();
        setRotation((float) Math.toDegrees(body.getAngle()));
        draw(batch);
    }
}
