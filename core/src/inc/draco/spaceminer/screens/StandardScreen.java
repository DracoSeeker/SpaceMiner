package inc.draco.spaceminer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Standard Screen that handles the basic operations that all screens will have to do,
 * such as clearing.
 * It is abstract so that its children can implement the methods to append to the basic operations
 */
public abstract class StandardScreen implements Screen {

    /**
     * The method implemented from {@link Screen} that is called every frame and draws the screen.  It sets the color and clears the screen to allow for the new frame to be drawn
     * @param delta the time taken for the curret frame, used for movement and physics
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0.93f, 0.46f, 1f); // sets the color to clear to fora all screens
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // clears using clear color
//        System.out.println("cleared");
        scRender(delta);
    }

    /**
     * The method that is implemented to actually draw the screen specific content.  It is called at the end of the render method implemented from {@link Screen}
     * @param delta The delta passed by the {@link Screen}.render()
     */
    public abstract void scRender(float delta);
}
