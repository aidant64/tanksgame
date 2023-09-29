package Screens;

import Actors.Actor;
import Actors.Entities.Island;
import Actors.Entities.Shell;
import Actors.Entities.Tank;
import Managers.ImageManager;
import Physics.Position;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class World {
    public static final float GAME_AREA_WIDTH = 8000, GAME_AREA_HEIGHT = 5000;

    public static final Rectangle GAME_AREA = new Rectangle(0,0, GAME_AREA_WIDTH, GAME_AREA_HEIGHT);
    public static final Rectangle TRAVERSABLE_AREA = new Rectangle(Tank.SHIP_WIDTH, Tank.SHIP_WIDTH,
            GAME_AREA_WIDTH- Tank.SHIP_WIDTH*2, GAME_AREA_HEIGHT- Tank.SHIP_WIDTH*2);

    private final Tank ship;

    private final ArrayList<Island> islands;
    private final Shell[] shells;

    private float elapsedTime = 0;

    public World(){

        ship =new Tank(400, 400);

        islands=new ArrayList<>();
        shells = new Shell[9];
        for(int i = 0; i < 9; i++){
            shells[i]=null;
        }

        generateIslands();
    }

    private void updateShells(float dt){

        for(int i = 0; i < 9; i++){
            Shell curShell = shells[i];

            if(curShell != null){
                curShell.update(dt);

                if(!Actor.isActorInGameArea(curShell)) shells[i] = null;
                if(curShell.isDone()) shells[i] = null;

                if(shells[i] != null){
                    for(Island island : islands){

                        if(curShell.isCollision(island)){
                            island.addCrater(curShell.getX(), curShell.getY());
                            shells[i]=null;
                            break;
                        }

                    }
                }

            }

        }

    }

    private void generateIslands(){
//        Random random = new Random();
//
//        int numIslands = 4 + random.nextInt(4);
//        final float maxIslandDimension = 1200;
//
//        for(int i = 0; i < numIslands; i++){
//            float x= 1000 + (GAME_AREA_WIDTH - 2000) * random.nextFloat();
//            float y= 1000 + (GAME_AREA_HEIGHT - 2000) * random.nextFloat();
//            float w = 300 + maxIslandDimension * random.nextFloat();
//            float r = random.nextFloat() * 360;
//
//            islands.add(new Island(x, y));
//        }

        islands.add(new Island(4000, 4000));

    }

    private void fireShells(Position targetPosition){

        for(int i = 0; i < ship.getTurrets().length; i++){

            Shell[] arr = ship.getTurrets()[i].fire(targetPosition);

            for(int j = 0; j < 3; j++){
                shells[3*i] = arr[0];
                shells[3*i+1] = arr[1];
                shells[3*i+2] = arr[2];
            }

        }

    }

    public void update(float deltaTime){
        elapsedTime += Gdx.graphics.getDeltaTime();

        ship.update(deltaTime);

        updateShells(deltaTime);
    }

    public void render(SpriteBatch batch){
        //ship.render(batch, elapsedTime);

        for(Island island : islands){
            island.render(batch);
        }

        for(Shell shell : shells){
            if(shell != null) shell.render(batch);
        }

        renderTraversableBorder(batch);
    }

    public void renderFilled(ShapeRenderer shapeRenderer){


        for(Island island : islands){
            island.renderFilled(shapeRenderer);
        }

    }

    public void render(ShapeRenderer shapeRenderer){

    }

    private void renderTraversableBorder(SpriteBatch batch){
        Sprite sprite = ImageManager.pixelSprite;
        sprite.setColor(Color.RED);
        sprite.setAlpha(.5f);
        sprite.setSize(30, 8);
        sprite.setRotation(0);
        sprite.setOriginCenter();

        for(float i = TRAVERSABLE_AREA.getX(); i < TRAVERSABLE_AREA.getX() + TRAVERSABLE_AREA.getWidth(); i+=60){
            sprite.setPosition(i, TRAVERSABLE_AREA.getY());
            sprite.draw(batch);

            sprite.setPosition(i, TRAVERSABLE_AREA.getY() + TRAVERSABLE_AREA.getHeight());
            sprite.draw(batch);
        }

        sprite.setSize(8, 30);
        for(float i = TRAVERSABLE_AREA.getY(); i < TRAVERSABLE_AREA.getY() + TRAVERSABLE_AREA.getHeight(); i+=60){
            sprite.setPosition(TRAVERSABLE_AREA.getX(), i);
            sprite.draw(batch);

            sprite.setPosition(TRAVERSABLE_AREA.getX() + TRAVERSABLE_AREA.getWidth(), i);
            sprite.draw(batch);
        }

    }

    public void keyDown(int keycode) {
        ship.keyDown(keycode);
    }

    public void keyUp(int keycode) {
        ship.keyUp(keycode);
    }

    public void touchDown(int gameX, int gameY, int pointer, int button) {

        if(button==0) fireShells(new Position(gameX, gameY));

    }

    public void mouseMoved(int gameX, int gameY) {

        ship.mouseMoved(gameX, gameY);

    }

    public void dispose(){

    }

    public Tank getShip() {
        return ship;
    }

    public void renderTiledBackground(SpriteBatch batch){

        final int tileW = 16*20;
        final int tileH = 16*20;

        for(int i = 0; i < GAME_AREA_WIDTH; i += tileW){

            for(int j = 0; j < GAME_AREA_HEIGHT; j+= tileH){
                TextureRegion r = ImageManager.waterAnimation.getKeyFrame(elapsedTime, true);
                batch.draw(r, i, j, tileW, tileH);
            }

        }

        //306082
//        Sprite s = ImageManager.pixelSprite;
//        s.setColor(Calc.makeColor(0x335491));
//        s.setSize(World.GAME_AREA_WIDTH, World.GAME_AREA_HEIGHT);
//        s.setPosition(0,0);
//        s.setAlpha(1f);
//        s.setOriginCenter();
//        s.setRotation(0);
//        s.draw(batch);

    }

}
