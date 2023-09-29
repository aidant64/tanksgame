package Actors.Entities;

import Actors.MovableActor;
import Managers.ImageManager;
import Physics.Calc;
import Physics.Position;
import Physics.Vector;
import Screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Turret extends MovableActor {
    private boolean justFired;

    private static final float TURRET_WIDTH=30,TURRET_HEIGHT=30;
    private static final float BARREL_WIDTH=35, BARREL_HEIGHT=6, BARREL_OFFSET=10;
    private static final float ROTATION_SPEED=0.55f;

    private final Position targetPosition;

    private final float turretType, spacing, beginLocalAngleRange, endLocalAngleRange;
    private float localAngle, aimAngle;

    public Turret(int turretType, float shipAngle) {
        super(0, 0, TURRET_WIDTH, TURRET_HEIGHT);
        targetPosition = new Position(-1, -1);

        switch (turretType){
            case 1:
                this.turretType=1;
                spacing=10f;
                beginLocalAngleRange =-110;
                endLocalAngleRange = 110;
                localAngle=0;
                break;
            case 2:
                this.turretType=2;
                spacing=60f;
                beginLocalAngleRange = -110;
                endLocalAngleRange = 110;
                localAngle=0;
                break;
            case 3:
                this.turretType=3;
                spacing=-75f;
                beginLocalAngleRange = 50;
                endLocalAngleRange = 310;
                localAngle=180;
                break;
            default:
                this.turretType=-1;
                spacing=-1;
                beginLocalAngleRange =-1;
                endLocalAngleRange =-1;
                break;
        }

        angle = shipAngle + localAngle;
        angle %= 360;
        updateBounds();
        aimAngle=0;
        justFired = false;
    }

    float elapsedTime;
    public boolean update(float dt, Tank ship) {
        updateTurretPosition(ship);

        //local angle can be negative
        localAngle += rotationVel;
        updateTurretRotationRelativeToShip(ship.getAngle());

        attemptRotateToDesiredAngle(ship.getAngle());
        adjustLocalAngleToBeInRange();

        updateBounds();

        if(justFired){
            elapsedTime += dt;
            if(elapsedTime > .2f){
                justFired = false;
            }
        }else{
            elapsedTime = 0;
        }

        //Gdx.input.
        Position p = GameScreen.unproject(Gdx.input.getX(), Gdx.input.getY());
        makeAimPath((int) p.getX(), (int) p.getY());

        return true;

    }

    public void makeAimPath(int screenX, int screenY){

        Position cursorPosition = new Position(screenX, screenY);

        Vector aimVector = Vector.makeVectorFrom(getPosition(), cursorPosition);
        aimAngle = aimVector.getDirection();
    }

    public void updateTurretRotationRelativeToShip(float shipAngle){

        localAngle = localAngle % 360;

        angle = shipAngle + localAngle;
        angle = angle % 360;
        if(angle<0){
            angle+=360;
        }

    }

    public Shell[] fire(Position targetPosition){

        if(!isTargetAcquired()){
            return new Shell[]{null, null, null};
        }

        justFired = true;
        Shell[] shells = new Shell[3];

        for(int i = 0; i < 3; i++){

            Position turretPosition = new Position(getX(), getY());

            float complementaryAngleOfTurret = (angle + 90) % 360;

            Vector barrelOffset = new Vector(complementaryAngleOfTurret, BARREL_OFFSET);
            Vector endOfBarrel = new Vector(angle, BARREL_WIDTH);
            turretPosition.add(endOfBarrel);

            shells[0]=new Shell(turretPosition, angle, targetPosition);
            shells[1]=new Shell(turretPosition.getPositionPlus(barrelOffset), angle, targetPosition);
            barrelOffset.setMagnitude(-BARREL_OFFSET);
            shells[2]=new Shell(turretPosition.getPositionPlus(barrelOffset), angle, targetPosition);


        }

        return shells;
    }

    private boolean isTargetAcquired(){
        return Math.abs(aimAngle - angle) < ROTATION_SPEED + 0.001f;
    }

    private void attemptRotateToDesiredAngle(float shipAngle){

        float desiredAngle = aimAngle - shipAngle;

        if(desiredAngle < 0){
            desiredAngle+=360;
        }
        desiredAngle %= 360;

        if(turretType == 1 || turretType == 2){
            if(Calc.getQuadrant(desiredAngle) == 4 || Calc.getQuadrant(desiredAngle) == 3){
                desiredAngle = desiredAngle - 360;
            }
        }

        if(desiredAngle > localAngle){
            rotationVel = 0.55f;
        }
        if(desiredAngle < localAngle){
            rotationVel = -0.55f;
        }

    }

    private void adjustLocalAngleToBeInRange(){
        if(localAngle<beginLocalAngleRange){
            localAngle=beginLocalAngleRange;

        }else if(localAngle>endLocalAngleRange){
            localAngle=endLocalAngleRange;
        }

        if(isTargetAcquired()) {
            angle = aimAngle;
            rotationVel = 0;
            rotationAcc = 0;
        }

    }

    private void updateTurretPosition(Tank ship){
        Position turPos = new Position(ship.getX(), ship.getY());

        Vector vector = new Vector(ship.getAngle(), spacing);
        turPos.add(vector);

        setPosition(turPos);
    }

    public boolean mouseMoved(int screenX, int screenY) {

        //makeAimPath(screenX, screenY);
        targetPosition.setPosition(screenX, screenY);

        return false;
    }

    public void render(SpriteBatch batch){
        ImageManager.turretTexture.setOriginCenter();
        ImageManager.turretTexture.setRotation(angle);
        ImageManager.turretTexture.setSize(w, h);
        ImageManager.turretTexture.setPosition(getX()-w/2, getY()-h/2);

        ImageManager.turretTexture.draw(batch);

        renderBarrels(batch);

    }

    private void renderBarrels(SpriteBatch batch){

        for(int i = 0; i < 3; i++){

            Position p = getPosition().getPositionPlus(new Vector(angle, BARREL_WIDTH/2));

            if(i == 1){
                p.add(new Vector((angle + 90) % 360, BARREL_OFFSET));
            }
            if(i == 2){
                p.add(new Vector((angle + 90) % 360, -BARREL_OFFSET));
            }

            Sprite sprite = ImageManager.pixelSprite;
            sprite.setColor(Calc.makeColor(0x46464d));
            sprite.setSize(BARREL_WIDTH, BARREL_HEIGHT);
            sprite.setPosition(p.getX() - BARREL_WIDTH/2, p.getY() - BARREL_HEIGHT/2);
            sprite.setOriginCenter();
            sprite.setRotation(angle);
            sprite.draw(batch);

        }
    }

    public void renderMuzzleFlash(SpriteBatch batch){
        if(justFired){

            final float width = 55;//40;
            final float height = 45;//30;

            Position position = new Position(getX(), getY()).getPositionPlus(new Vector(angle, 35 + width/2));

            TextureRegion r = ImageManager.fireAnimation.getKeyFrame(elapsedTime, true);

            batch.draw(r, position.getX() - width/2, position.getY() - height/2,
                    width/2, height/2,
                    width, height,
                    1f, 1f, angle);

        }
    }

    private void renderAimIndicator(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.RED);
        if(isTargetAcquired()) {
            shapeRenderer.setColor(Color.GREEN);
        }

        shapeRenderer.circle(getX(), getY(), 4);
    }

}
