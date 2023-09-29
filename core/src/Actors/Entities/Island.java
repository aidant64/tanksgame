package Actors.Entities;

import Actors.Actor;
import Managers.ImageManager;
import Physics.Position;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Island extends Actor {
    private final ArrayList<Position> craters;

    public Island(float x, float y) {
        super(x, y, 2000, 1000);

        craters=new ArrayList<>();
    }
    public void addCrater(float x, float y){
        craters.add(new Position(x, y));
    }

    public void renderFilled(ShapeRenderer shapeRenderer) {

        //renderMainBodyFilled(shapeRenderer, Calc.makeColor(0xffad8a));

        shapeRenderer.setColor(Color.BROWN);
        for(Position crater : craters){
            shapeRenderer.circle(crater.getX(), crater.getY(), 8);
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(ImageManager.islandTexture, getX() - w/2, getY() - h/2, w, h);
    }
}
