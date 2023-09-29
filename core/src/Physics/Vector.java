package Physics;

public class Vector {
    private static final float APPROXIMATE_ZERO=0.09f;
    private float direction, magnitude;

    public Vector(){
        direction=0;
        magnitude=0;
    }

    public Vector(float direction, float magnitude){
        this.direction=direction;
        this.magnitude=magnitude;
    }

    public void set(float direction, float magnitude){
        this.direction=direction;
        this.magnitude=magnitude;
    }

    public void reset(){
        direction=0;
        magnitude=0;
    }

    public float getXComponent(){
        return magnitude * (float) Math.cos(Math.toRadians(direction));//i
    }

    public float getYComponent(){
        return magnitude * (float) Math.sin(Math.toRadians(direction));//j
    }

    public void scale(float scalar){
        magnitude*=scalar;
    }

    public void normalize(){
        magnitude=1;
    }

    public Vector getNormalizedVector() {
        return new Vector(direction, 1);
    }

    public void combine(Vector vector){
        if(vector.magnitude==0){
            return;
        }

        float iComponent=getXComponent()+vector.getXComponent();
        float jComponent=getYComponent()+vector.getYComponent();

        setVectorFromComponents(iComponent, jComponent);

    }

    public void setVectorFromComponents(float xComponent, float yComponent){
        float[] arr = calculateVector(xComponent,yComponent);
        direction=arr[0];
        magnitude=arr[1];
    }

    public static Vector combine(Vector f1, Vector f2){
        if(f1.magnitude==0 &&  f2.magnitude==0){
            return new Vector();
        } else if(f2.magnitude==0){
            return f1;
        } else if(f1.magnitude==0){
            return f2;
        }

        float iComponent=f1.getXComponent()+f2.getXComponent();
        float jComponent=f1.getYComponent()+f2.getYComponent();

        return makeVectorFromComponents(iComponent, jComponent);
    }

    public static Vector makeScaledVector(Vector vector, float scalar){
        return new Vector(vector.direction, vector.magnitude * scalar);
    }

    public static Vector makeVectorFromComponents(float xComponent, float yComponent){
        float[] arr = calculateVector(xComponent,yComponent);
        return new Vector(arr[0], arr[1]);
    }

    public static Vector makeVectorFrom(Position p1, Position p2){
        float xComp = p2.getX()-p1.getX();
        float yComp = p2.getY()-p1.getY();

        return Vector.makeVectorFromComponents(xComp, yComp);
    }

    public static Vector makeNormalizedVectorFrom(float direction){
        return new Vector(direction, 1);
    }

    public void zeroXComponent(){

    }

    private static float[] calculateVector(float xComponent, float yComponent){

        float resultingMagnitude = (float) Math.sqrt(xComponent*xComponent + yComponent*yComponent);
        float resultingDirection;
        if(Math.abs(xComponent)==0){
            resultingDirection = (yComponent>0) ? 90 : 270;

        }else if(Math.abs(yComponent)==0){
            resultingDirection = (xComponent>0) ? 0 : 180;

        }else{
            resultingDirection = (float) Math.toDegrees(Math.atan(Math.abs(yComponent)/Math.abs(xComponent)));

            if(xComponent<0 && yComponent>0){
                //quadrant 2
                resultingDirection = 180 - resultingDirection;

            }else if(xComponent<0&&yComponent<0){
                //quadrant 3
                resultingDirection = resultingDirection + 180;

            }else if(xComponent>0&&yComponent<0){
                //quadrant 4
                resultingDirection = 360 - resultingDirection;
            }
        }

        return new float[] {resultingDirection, resultingMagnitude};
    }

    public static Vector makeNormalizedVectorFrom(Vector vector){
        return new Vector(vector.getDirection(),1);
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public boolean isApproximatelyZero(){
        return Math.abs(magnitude) < APPROXIMATE_ZERO;
    }

    public void approximateToZeroIfNecessary(){
        if(Math.abs(magnitude) < APPROXIMATE_ZERO) magnitude=0;
    }

    private boolean isApproximatelyZero(float f){
        return Math.abs(f) < APPROXIMATE_ZERO;
    }

}
