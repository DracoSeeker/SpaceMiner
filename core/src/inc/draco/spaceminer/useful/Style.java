package inc.draco.spaceminer.useful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public final class Style {
    public static Skin defaultSkin;
    public static void createStyles() {
        Pixmap pix = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        defaultSkin = new Skin();
        defaultSkin.add("title", createLabelSkin(128, "Fonts/menzanine/Menzanine.ttf"));
        defaultSkin.add("paragraph", createLabelSkin(32, "Fonts/menzanine/Menzanine.ttf"));
        pix.dispose();
    }

    private static TextButton.TextButtonStyle createTextButtonSkin(Pixmap pix)   {
        pix.setColor(Color.rgba8888(.5f, .5f, .5f, 1f));
        pix.fill();
        NinePatch up = new NinePatch(new Texture(pix), 10, 10, 10, 10);

        pix.setColor(Color.rgba8888(.2f, .2f, .2f, 1f));
        pix.fill();
        NinePatch down = new NinePatch(new Texture(pix), 10, 10, 10, 10);

        BitmapFont textButtonFont = getBitmapFont(80, "Fonts/breathe_fire/Breathe Fire.otf");
        return new TextButton.TextButtonStyle(new NinePatchDrawable(up), new NinePatchDrawable(down), new NinePatchDrawable(down), textButtonFont);
    }

    private static BitmapFont getBitmapFont(int size, String font) {
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.genMipMaps = true;
        params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        params.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(font));
        BitmapFont textButtonFont = gen.generateFont(params);
        gen.dispose();
        return textButtonFont;
    }

    private static Label.LabelStyle createLabelSkin(int size, String font)  {
        return new Label.LabelStyle(getBitmapFont(size, font), Color.WHITE);
    }

    private static Label.LabelStyle createLabelSkinWithSkin(int size, Pixmap pix, Color c, int cornerRad)  {
        Label.LabelStyle l = new Label.LabelStyle(getBitmapFont(size, "Fonts/breathe_fire/Breathe Fire.otf"), Color.BLACK);
        pix.setColor(Color.CLEAR);
        pix.fill();
        pix.setColor(c);
//        pix.drawRectangle(0,0, 100, 100);
        pix.fillRectangle(cornerRad, 0, 100 - (2 * cornerRad), 100);
        pix.fillRectangle(0, cornerRad, 100, 100 - (2 * cornerRad));
        pix.fillCircle(cornerRad, cornerRad, cornerRad);
        pix.fillCircle(100 - cornerRad, cornerRad, cornerRad);
        pix.fillCircle(cornerRad, 100 - cornerRad, cornerRad);
        pix.fillCircle(100 - cornerRad, 100 - cornerRad, cornerRad);
        PixmapIO.writePNG(Gdx.files.absolute("D:/inteliJ/pix.png"), pix);
        l.background = new NinePatchDrawable(new NinePatch(new Texture(pix), cornerRad, cornerRad, cornerRad, cornerRad));
//        l.background = new TextureRegionDrawable(new Texture(pix));
        return l;

    }
}
