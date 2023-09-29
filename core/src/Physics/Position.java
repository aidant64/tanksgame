package Physics;

public class Position {
    private float x,y;

    public Position(float x, float y){
        this.x=x;
        this.y=y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector){
        x+=vector.getXComponent();
        y+=vector.getYComponent();
    }

    public Position getPositionPlus(Vector vector){
        return new Position(x+vector.getXComponent(), y+vector.getYComponent());
    }

    public Vector getVectorTo(Position position){
        return Vector.makeVectorFrom(this, position);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
