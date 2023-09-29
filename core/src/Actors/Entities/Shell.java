package Actors.Entities;

import Actors.MovableActor;
import Managers.ImageManager;
import Physics.Calc;
import Physics.Position;
import Physics.Vector;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Shell extends MovableActor {
    private static final float SHELL_WIDTH = 6, SHELL_HEIGHT = 6, FIRED_FORCE=1400f;
    private float elapsedTime;

    private Position startingPosition, targetPosition;
    private boolean renderTrail;
    private float trailLength;

    private boolean done, hitTarget;

    public Shell(Position startingPosition, float direction, Position targetPosition){
        super(startingPosition.getX(), startingPosition.getY(), SHELL_WIDTH, SHELL_HEIGHT);
        this.startingPosition = startingPosition;
        this.targetPosition = targetPosition;

        renderTrail = false;
        trailLength = 200;

        Vector firedForce = Vector.makeNormalizedVectorFrom(direction);
        firedForce.scale(FIRED_FORCE);
        velocity.combine(firedForce);

        angle = firedForce.getDirection();
        updateBounds();

        done = false;
        hitTarget = false;
        elapsedTime = 0;

        this.targetPosition.add(new Vector(angle, 2));
    }

    public boolean update(float deltaTime) {

        if(!hitTarget){
            position.add(Vector.makeScaledVector(velocity, deltaTime));

            updateBounds();

            //boolean var used to prevent distance checking the whole lifetime
            if(!renderTrail){
                if(Calc.distanceBetween(position, startingPosition) > 300){
                    renderTrail = true;
                }
            }

            checkReachedTarget();

            trailLength += 1f;
        }else{
            renderTrail=true;
            trailLength /= 2f;

            elapsedTime += deltaTime;

            if(ImageManager.splashAnimation.isAnimationFinished(elapsedTime)){
                done = true;
            }
        }

        return false;
    }

    private void checkReachedTarget(){
        if (Calc.distanceBetween(position, targetPosition) < 50f){
            hitTarget = true;
            velocity.reset();
        }
    }

    public void render(SpriteBatch batch){
        if(renderTrail){
            renderTrail(batch, trailLength);
        }else{
            renderTrail(batch, Calc.distanceBetween(position, startingPosition));
        }

        if(!hitTarget){
            Sprite sprite = ImageManager.pixelSprite;
            sprite.setColor(Color.RED);
            //sprite.setPosition(getX() - w/2, getY() - h/2);
            sprite.setSize(w, h);
            sprite.setCenter(getX(), getY());
            sprite.setOriginCenter();
            sprite.setRotation(angle);

            sprite.draw(batch);
        }else{
            TextureRegion r = ImageManager.splashAnimation.getKeyFrame(elapsedTime, false);
            Sprite sprite = new Sprite(r);
            sprite.setAlpha(0.4f - elapsedTime);
            sprite.setSize(16*2.5f, 16*2.5f);
            sprite.setCenter(position.getX(), position.getY());
            sprite.setOriginCenter();
            sprite.setRotation(angle);
            sprite.draw(batch);
        }

        //renderTarget(batch);
    }

    private void renderTrail(SpriteBatch batch, float length){
        Sprite sprite = ImageManager.tracerSprite;

        Position p  = new Position(getX(), getY()).getPositionPlus(new Vector(angle, -length/2));
        sprite.setPosition(p.getX()-length/2f, p.getY()-5/2f);
        sprite.setSize(length, 5);
        sprite.setOriginCenter();
        sprite.setRotation(angle);
        sprite.setAlpha(.15f);

        sprite.draw(batch);
    }

    private void renderTarget(SpriteBatch batch){
        Sprite sprite = ImageManager.pixelSprite;
        sprite.setColor(Color.YELLOW);

        sprite.setPosition(targetPosition.getX(), targetPosition.getY());
        sprite.setSize(5, 5);
        sprite.setOriginCenter();
        //sprite.setRotation(angle);

        sprite.draw(batch);
    }

    public boolean isDone(){
        return done;
    }


}
