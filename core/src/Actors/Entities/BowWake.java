package Actors.Entities;

import Managers.ImageManager;
import Physics.Position;
import Physics.Vector;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class BowWake {
    private static final float w = 14, h = 14;
    private final Vector[] wakePositions;

    private final Position position;
    private final float angle;
    private float timer;

    public BowWake(float x, float y, float angle, boolean isShipMoving){
        position = new Position(x,y);
        this.angle = angle;
        timer = 0;

        wakePositions = new Vector[]{
                new Vector(angle + 2, 142), new Vector(angle - 2, 142),
                new Vector(angle + 8, 135), new Vector(angle - 8, 135),
                new Vector(angle + 5, 140), new Vector(angle - 5, 140),
                new Vector(angle + 12, 122),  new Vector(angle - 12, 122),
                new Vector(angle + 15, 110),  new Vector(angle - 15, 110),
                new Vector(angle + 130, 45), new Vector(angle - 130, 45),
                new Vector(angle + 110, 37), new Vector(angle - 110, 37),
                new Vector(angle + 90, Tank.SHIP_HEIGHT / 2 + h / 2),
                new Vector(angle - 90, Tank.SHIP_HEIGHT / 2 + h / 2),
                new Vector(angle + 50, 45), new Vector(angle - 50, 45),
                new Vector(angle + 40, 55), new Vector(angle - 40, 55),
                new Vector(angle + 30, 70), new Vector(angle - 30, 70),
                new Vector(angle + 20f, 85), new Vector(angle - 20f, 85),
                new Vector(angle + 10, 120), new Vector(angle - 10, 120),
                new Vector(angle, Tank.SHIP_WIDTH / 2 + w)};

        adjustPosition(isShipMoving);
    }

    private void adjustPosition(boolean isShipMoving){
        Random r = new Random();
        Vector v = wakePositions[r.nextInt(wakePositions.length)];

        if(!isShipMoving){
            if(r.nextBoolean()){
                v.setMagnitude(-v.getMagnitude());
            }
        }

        position.add(v);
    }

    public void update(float deltaTime){
        timer += deltaTime;
    }

    public void render(SpriteBatch spriteBatch){
        Sprite sprite = ImageManager.pixelSprite;
        sprite.setColor(Color.WHITE);
        float alpha = (timer - 2) / 1.5f;
        if(alpha < 0) alpha = 0;
        if(alpha >= .4f) {
            alpha = .4f;
            sprite.setColor(Color.RED);
        }
        sprite.setAlpha(.4f - alpha);
        sprite.setSize(w, h);
        sprite.setOriginCenter();
        sprite.setRotation(angle);
        sprite.setCenter(position.getX(), position.getY());

        sprite.draw(spriteBatch);
    }

    public boolean isDone(){
        return timer < 0;
    }

}
