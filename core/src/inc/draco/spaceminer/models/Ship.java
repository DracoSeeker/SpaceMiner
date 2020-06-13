package inc.draco.spaceminer.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import inc.draco.spaceminer.SpaceMiner;
import inc.draco.spaceminer.info.Info;

public class Ship extends Sprite {

    private SpaceMiner spaceMiner;
    private Viewport viewport;
    private Camera camera;

    private Texture texture;

    private Body body;

    private Vector2 pointer;
    private Vector3 pos;

    public float speed = .8f; //Speed in as percentage of max
    public float turn = .9f; //Turning rate as proportion of max
    public float boost = 1f; //Speed as percentage of max

    public Ship(SpaceMiner spaceMiner, Viewport view, World world) {
        super(spaceMiner.manager.get("Ships/SmallShip.png", Texture.class));
        this.spaceMiner = spaceMiner;
        viewport = view;
        camera = view.getCamera();

        texture = getTexture();

        setTexture(texture);
        setSize(1,1);
        setCenter(0, 0);
        setOriginCenter();

        pointer = new Vector2(0,0);
        pos = new Vector3(0,0,0);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.gravityScale = 0;
        def.linearDamping = .99f;
        def.angularDamping = .99f;
        def.position.set(0, 0);

        body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth() / 2);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 2f;
        fixDef.restitution = 0;

        body.createFixture(fixDef);

        force = new Vector2(0,100);
    }

    private Vector2 force;

    public void update(float delta) {

        pointer.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(pointer);
        pointer.add(-body.getPosition().x, -body.getPosition().y);

        float mouseAng = (float) (Math.atan2(pointer.y, pointer.x));
//        System.out.println("mouseAng = " + mouseAng);
        if (mouseAng < 0) {
            mouseAng = (float) ((2f * Math.PI) + mouseAng);
        }
//        float mouseAng = pointer.angleRad();
//        System.out.println("mouseAng = " + Math.toDegrees(mouseAng));

        float bodyAng  = ((body.getAngle() + (Info.PI / 2f))) % (Info.PI *2f);
//        System.out.println("bodyAng(Raw) = " + Math.toDegrees(bodyAng));
        if (bodyAng < 0) bodyAng = (float) (2f * Math.PI + bodyAng);

//        bodyAng += Info.PI / 2f;

//        System.out.println("bodyAng = " + Math.toDegrees(bodyAng));

        float deltaRotation = (mouseAng - bodyAng);

//        System.out.println("deltaRotation(Raw) = " + Math.toDegrees(deltaRotation));

        if (deltaRotation > Info.PI) {
            deltaRotation = -((2f * Info.PI) - deltaRotation);
//            System.out.println(">");
        } else if (deltaRotation < -Info.PI) {
            deltaRotation = ((2f * Info.PI) + deltaRotation);
//            System.out.println("<");
        }

//        System.out.println("deltaRotation = " + Math.toDegrees(deltaRotation));

        body.setLinearDamping(.8f);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            body.setLinearDamping(1f);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            pos.add((float) (-Math.cos(Math.toRadians(getRotation() - 90)) * speed * delta), (float) (-Math.sin(Math.toRadians(getRotation() - 90)) * speed * delta), 0);


            float impulse = turn * ((deltaRotation) * .2f - body.getAngularVelocity() * .022f);
            body.applyAngularImpulse(impulse, true);
            force.setLength(speed * .16f);
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) force.setLength(force.len() * (1 + boost) * 2f);
            force.setAngleRad(body.getAngle() + Info.PI / 2f);
            body.applyLinearImpulse(force, body.getWorldCenter(), true);
        }



//        if (pos.x > Info.WORLD_WIDTH / 2) pos.x = Info.WORLD_WIDTH / 2;
//        if (pos.x < -Info.WORLD_WIDTH / 2) pos.x = -Info.WORLD_WIDTH / 2;
//        if (pos.y > Info.WORLD_HEIGHT / 2) pos.y = Info.WORLD_HEIGHT / 2;
//        if (pos.y < -Info.WORLD_HEIGHT/ 2) pos.y = -Info.WORLD_HEIGHT / 2;


        setCenter(body.getPosition().x, body.getPosition().y);


        setRotation((float) Math.toDegrees(body.getAngle()));

        camera.position.set(body.getPosition().x, body.getPosition().y, 0);
    }

    public float getSpeed() {
        return Math.abs(body.getLinearVelocity().len());
    }

}
