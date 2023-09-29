package Actors.Entities;

import Actors.MovableActor;
import Managers.WakeManager;
import Physics.Calc;
import Physics.Vector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tank extends MovableActor {

    private float elapsedTime;
    public static final float FRICTION_UNITS=.4f, MAX_ROTATION_VELOCITY=1,
            SHIP_WIDTH = 200, SHIP_HEIGHT = 200;
    protected final static float MAX_ENGINE_FORCE = 1.5f;

    protected Vector inputForce, dragForce, momentumForce;

    private final Turret[] turrets;
    private final WakeManager wakeManager;

    public Tank(float x, float y) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT);
        inputForce=new Vector();
        dragForce=new Vector();
        momentumForce=new Vector();

        turrets = new Turret[3];
        for(int i = 1; i < 4; i++){
            turrets[i-1] = new Turret(i, getAngle());
        }

        elapsedTime = 0;

        wakeManager = new WakeManager();
    }

    public void update(float deltaTime) {

        updateForceVectors();

        combineForceVectors();

        acceleration = accumulatedForce;
        velocity.combine(acceleration);

        position.add(Vector.makeScaledVector(velocity, deltaTime));
        if(Calc.isApproxZero(velocity.getMagnitude(), 3f) && inputForce.getMagnitude() == 0)
            velocity.setMagnitude(0);
        keepInTraversableArea();

        updateRotation();
        updateBounds();

        for(Turret turret : turrets){
            turret.update(deltaTime, this);
        }

        wakeManager.update(deltaTime, this);

    }


    private void combineForceVectors(){
        accumulatedForce.reset();
        accumulatedForce.combine(inputForce);
        accumulatedForce.combine(dragForce);
        accumulatedForce.combine(momentumForce);
    }

    private void updateForceVectors(){
        if(isMoving()){
            dragForce.set(velocity.getDirection()-180, velocity.getMagnitude() * .01f);
        }else{
            dragForce.reset();
        }

        if(isMoving()){
            momentumForce.set(angle, velocity.getMagnitude() * .0025f);
        }else{
            momentumForce.reset();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {

            inputForce.setDirection(angle);
            inputForce.setMagnitude(inputForce.getMagnitude() + 0.02f);
            if (inputForce.getMagnitude() > MAX_ENGINE_FORCE) {
                inputForce.setMagnitude(MAX_ENGINE_FORCE);
            }

        }else if(Gdx.input.isKeyPressed(Input.Keys.S)){

            inputForce.setDirection((angle+180)%360);
            inputForce.setMagnitude(inputForce.getMagnitude() + 0.02f);
            if (inputForce.getMagnitude() > MAX_ENGINE_FORCE) {
                inputForce.setMagnitude(MAX_ENGINE_FORCE);
            }

        }else{
            inputForce.reset();
        }
    }

    private void updateRotation(){

        if(Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)){
            rotationAcc = 0;

        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {

            rotationAcc=-.05f;

        }else if(Gdx.input.isKeyPressed(Input.Keys.A)) {

            rotationAcc=.05f;

        } else {
            rotationAcc = 0;
        }

        rotationVel+=rotationAcc;
        if(rotationVel>MAX_ROTATION_VELOCITY){
            rotationVel=MAX_ROTATION_VELOCITY;
        }else if(rotationVel<-MAX_ROTATION_VELOCITY){
            rotationVel=-MAX_ROTATION_VELOCITY;
        }
        if(rotationAcc==0){
            if(rotationVel<0){
                rotationVel = (Math.abs(rotationVel)<0.6f) ? 0 : rotationVel+FRICTION_UNITS;
            }else{
                rotationVel = (Math.abs(rotationVel)<0.6f) ? 0 : rotationVel-FRICTION_UNITS;
            }
        }

        float rvel = rotationVel * (velocity.getMagnitude() / 500);
        //if (rvel > MAX_ROTATION_VELOCITY) rvel = MAX_ROTATION_VELOCITY;
        angle += rvel;
        angle = angle % 360;
        if(angle < 0){
            angle += 360;
        }
    }

    public boolean keyDown(int keycode){

//        switch (keycode){
//            case Input.Keys.D:
//                rotationAcc=-.05f;
//                break;
//            case Input.Keys.A:
//                rotationAcc=.05f;
//                break;
//            case Input.Keys.W:
//                //wKeyDown=true;
//                break;
//            case Input.Keys.S:
//                //inputForce.set(angle-180, MAX_ENGINE_FORCE);
//                break;
//        }

        return false;
    }

    public boolean keyUp(int keycode){

//        switch (keycode){
//            case Input.Keys.W:
//                //inputForce.reset();
//                break;
//            case Input.Keys.S:
//                //inputForce.reset();
//                break;
//            case Input.Keys.A:
//            case Input.Keys.D:
//                rotationAcc=0;
//                break;
//        }

        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {

        for(Turret turret : turrets){
            turret.mouseMoved(screenX, screenY);
        }

        return false;
    }

   public void render(ShapeRenderer renderer, float dt) {

   }

    public Turret[] getTurrets() {
        return turrets;
    }

    public Vector getInputForce() {
        return inputForce;
    }

    public Vector getDragForce() {
        return dragForce;
    }

}
