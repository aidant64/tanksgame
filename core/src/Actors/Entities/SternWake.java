package Actors.Entities;

import Managers.ImageManager;
import Physics.Position;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SternWake {
    private final Position position;
    private float timer, elapsedTime;
    private final float angle;
    private static final float aspectRatio = .5f;
    private float w, h;//40, 80


    public SternWake(float x, float y, float angle, float startingWidth){
        position = new Position(x, y);
        this.angle = angle;
        w = startingWidth;
        h = w / aspectRatio;
    }

    public Position getPosition(){
        return position;
    }

    public void update(float dt){
        elapsedTime += dt;

        timer += dt;

        if(timer > .1f){
            h+=1f;

            timer = 0;
        }
    }

    public float getElapsedTime(){
        return elapsedTime;
    }

    public void render(SpriteBatch batch){

        if(elapsedTime < 2.6f){
            Sprite sprite = ImageManager.sternWake;
            sprite.setColor(Color.WHITE);
            sprite.setSize(w, h);
            sprite.setCenter(position.getX(), position.getY());

            sprite.setOriginCenter();
            sprite.setRotation(angle);

            float alpha = .6f - (elapsedTime / 3f);
            if(alpha < 0) alpha = 0;
            sprite.setAlpha(alpha);

            sprite.draw(batch);
        }


    }

}
