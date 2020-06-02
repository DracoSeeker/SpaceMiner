package inc.draco.spaceminer.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inc.draco.spaceminer.SpaceMiner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Space Miner");
		config.setWindowIcon(Files.FileType.Internal, "Icons/SmallShipIcon.png");
		int width = 1300;
		config.setWindowedMode(width, width * 1080 / 1920);
		config.setWindowPosition(100, 50);
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode(Lwjgl3ApplicationConfiguration.getMonitors()[1]));

		new Lwjgl3Application(new SpaceMiner(), config);
	}
}
