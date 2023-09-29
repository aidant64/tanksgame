package Actors;

import Physics.Position;
import Physics.Vector;
import Screens.World;

public class MovableActor extends Actor {

    protected float angle, rotationVel, rotationAcc;
    protected Vector acceleration, velocity, accumulatedForce;

    public MovableActor(float x, float y, float w, float h) {
        super(x, y, w, h);

        acceleration=new Vector();
        velocity=new Vector();
        accumulatedForce=new Vector();
    }

    @Override
    protected void updateBounds() {
        super.updateBounds();
        bounds.setRotation(angle);
    }

    public boolean isMoving() {
        return Math.abs(velocity.getXComponent()) > 0 || Math.abs(velocity.getYComponent()) > 0;
    }

    public void setPosition(float x, float y){
        position.setPosition(x,y);
        updateBounds();
    }

    public void setPosition(Position position){
        this.position.setPosition(position.getX(),position.getY());
        updateBounds();
    }

    public void setX(float x) {
        position.setX(x);
        updateBounds();
    }

    public void setY(float y) {
        position.setY(y);
        updateBounds();
    }

    protected void keepInTraversableArea(){
        if(getX()< World.TRAVERSABLE_AREA.getX()){
            setX(World.TRAVERSABLE_AREA.getX());
            velocity.setVectorFromComponents(0, velocity.getYComponent());

        } else if(getX()>World.TRAVERSABLE_AREA.getX()+World.TRAVERSABLE_AREA.getWidth()){
            setX(World.TRAVERSABLE_AREA.getX()+World.TRAVERSABLE_AREA.getWidth());
            velocity.setVectorFromComponents(0, velocity.getYComponent());

        }

        if(getY()< World.TRAVERSABLE_AREA.getY()){
            setY(World.TRAVERSABLE_AREA.getY());
            velocity.setVectorFromComponents(velocity.getXComponent(), 0);

        } else if(getY()>World.TRAVERSABLE_AREA.getY()+World.TRAVERSABLE_AREA.getHeight()){

            setY(World.TRAVERSABLE_AREA.getY()+World.TRAVERSABLE_AREA.getHeight());
            velocity.setVectorFromComponents(velocity.getXComponent(), 0);
        }
    }

    public float getAngle() {
        return angle;
    }

    public float getRotationVel() {
        return rotationVel;
    }

    public void setRotationVel(float rotationVel) {
        this.rotationVel = rotationVel;
    }

    public float getRotationAcc() {
        return rotationAcc;
    }

    public void setRotationAcc(float rotationAcc) {
        this.rotationAcc = rotationAcc;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAccumulatedForce() {
        return accumulatedForce;
    }
}
