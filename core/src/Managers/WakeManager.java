package Managers;

import Actors.Entities.BowWake;
import Actors.Entities.SternWake;
import Actors.Entities.Tank;
import Physics.Calc;
import Physics.Position;
import Physics.Vector;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WakeManager {
    private static final int NUM_BOW_WAKES = 50;

    private float bowWakeTimer;
    private int bowWakeIndex;
    private final BowWake[] bowWakes;

    private final SternWake[] sternWakes;
    private int sternWakeIndex;

    public WakeManager(){
        bowWakes = new BowWake[NUM_BOW_WAKES];
        bowWakeTimer = 0;
        bowWakeIndex = 0;

        sternWakes = new SternWake[20];
        sternWakes[0] = new SternWake(-1000, -1000, 90, 1);
        sternWakeIndex=0;
    }

    public void update(float dt, Tank ship){
        bowWakeTimer += dt;

        if(ship.getVelocity().getMagnitude() > 80){
            if(bowWakeTimer > .04f){
                spawnBowWake(ship, true);
                bowWakeTimer = 0;
            }
        }else{
            if(bowWakeTimer > .4f){
                spawnBowWake(ship, false);
                bowWakeTimer = 0;
            }
        }


        for(BowWake bowWake : bowWakes){
            if(bowWake != null) bowWake.update(dt);
        }

        spawnSternWakes(dt, ship);
        updateSternWakes(dt);
    }

    private void spawnBowWake(Tank ship, boolean isMoving){
        bowWakeIndex++;
        if(bowWakeIndex >= NUM_BOW_WAKES) bowWakeIndex = 0;
        bowWakes[bowWakeIndex] = new BowWake(ship.getX(), ship.getY(), ship.getAngle(), isMoving);
    }

    private void spawnSternWakes(float deltaTime, Tank ship){
        if(ship.isMoving()){
            Position positionOfStern = new Position(ship.getX(), ship.getY()).getPositionPlus(
                    new Vector(ship.getAngle(), -(ship.getW()/2 - 20)));

            float distance = 1000, size = 0;
            if(ship.getVelocity().getMagnitude() > 100){
                distance = 23;
                size = 28;
            }else if(ship.getVelocity().getMagnitude() > 50){
                distance = 15;
                size = 25;
            }else if(ship.getVelocity().getMagnitude() > 20){
                distance=10;
                size=20;
            }

            if(Calc.distanceBetween(positionOfStern, sternWakes[sternWakeIndex].getPosition()) > distance){
                sternWakeIndex++;
                if(sternWakeIndex>19) sternWakeIndex = 0;

                sternWakes[sternWakeIndex] = new SternWake(positionOfStern.getX(), positionOfStern.getY(),
                        ship.getAngle(), size);
            }
        }

    }

    private void updateSternWakes(float deltaTime){
        for(SternWake sternWake : sternWakes){
            if(sternWake != null){
                sternWake.update(deltaTime);

                if(sternWake.getElapsedTime() > 2f){
                    //sternWakes.remove(sternWake);
                }
            }

        }
    }

    public void render(SpriteBatch batch){
        for(BowWake bowWake : bowWakes){
            if(bowWake != null) bowWake.render(batch);
        }

        for(SternWake sternWake : sternWakes){
            if(sternWake != null) sternWake.render(batch);
        }
    }

}
