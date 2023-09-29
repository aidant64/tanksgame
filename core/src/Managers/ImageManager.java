package Managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {

    public static Animation<TextureRegion> waterAnimation;
    public static Animation<TextureRegion> splashAnimation;
    public static Animation<TextureRegion> fireAnimation;

    public static TextureRegion islandTexture;

    public static TextureRegion shipTexture;
    public static Sprite turretTexture;
    public static Sprite pixelSprite;
    public static Sprite tracerSprite;
    public static Sprite sternWake;

    public static void loadImages(){
        Texture waterTexture = new Texture("images/waterFrames.png");
        TextureRegion[][] frames = TextureRegion.split(waterTexture, 16, 16);
        TextureRegion[] waterFrames = new TextureRegion[5];
        for(int i = 0; i < 5; i++){
            waterFrames[i] = frames[0][i];
        }
        waterAnimation = new Animation(.75f, waterFrames);

        TextureRegion[][] shipFrameTextures = TextureRegion.split(new Texture("images/splash.png"), 16, 16);
        TextureRegion[] shipFrames = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            shipFrames[i] = shipFrameTextures[0][i];
        }
        splashAnimation = new Animation(.1f, shipFrames);

        Texture fireAnimTexture = new Texture("images/fireFrames.png");
        TextureRegion[][] fireFrameTextures = TextureRegion.split(fireAnimTexture, 8, 6);
        TextureRegion[] fireFrames = new TextureRegion[4];
        for(int i = 0; i < 4; i++){
            fireFrames[i] = fireFrameTextures[0][i];
        }
        fireAnimation = new Animation(.02f, fireFrames);


        Texture ship = new Texture("images/ship.png");
        shipTexture = new Sprite(ship, 0, 0, 40, 9);

        Texture turret = new Texture("images/turret.png");
        turretTexture = new Sprite(turret, 0, 0, 6, 6);

        Texture island = new Texture("images/island.png");
        islandTexture = new Sprite(island, 0, 0, 100, 50);

        Texture wake = new Texture("images/wake.png");
        pixelSprite = new Sprite(wake, 0, 0, 1, 1);

        Texture tracer = new Texture("images/tracer.png");
        tracerSprite = new Sprite(tracer, 0, 0, 20, 3);

        sternWake = new Sprite(new Texture("images/sternWake.png"), 0, 0, 4, 8);
    }


}
