package Actors;

import Physics.Position;
import Screens.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Actor {
    protected Position position;
    protected final float w, h;
    protected Polygon bounds;

    public Actor(float x, float y, float w, float h){
        position=new Position(x,y);
        this.w=w;
        this.h=h;
        bounds=new Polygon();
        updateBounds();
    }

    public void render(ShapeRenderer shapeRenderer){}

    public boolean isCollision(Actor actor) {
        return Intersector.overlapConvexPolygons(bounds, actor.getBounds());
    }

    public static boolean isActorInGameArea(Actor actor){
        Polygon gameArea = new Polygon();
        gameArea.setVertices(new float[] {0,0, World.GAME_AREA_WIDTH, 0,
                World.GAME_AREA_WIDTH, World.GAME_AREA_HEIGHT,0,World.GAME_AREA_HEIGHT});

        return Intersector.overlapConvexPolygons(actor.getBounds(), gameArea);
    }

    protected void updateBounds(){

        final float x = position.getX(), y=position.getY();

        float[] vertices = {
                (x-w/2), (y-h/2) + (h*.66f),
                (x-w/2), (y-h/2) + (h*.33f),

                x - (w/2) + (w*.33f), (y-h/2),
                x - (w/2) + (w*.66f), (y-h/2),

                (x + w/2), y - (h/2) + (h*.33f),
                (x + w/2), y - (h/2) + (h*.66f),

                (x-w/2) + (w*.66f), (y+h/2),
                (x-w/2) + (w*.33f), (y+h/2)
        };

        bounds.setVertices(vertices);
        bounds.setOrigin(x,y);
    }

    public Position getPosition() {
        return position;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public Polygon getBounds() {
        return bounds;
    }

    protected void renderMainBodyFilled(ShapeRenderer shapeRenderer, Color color){
        float[] a = getBounds().getTransformedVertices();

        shapeRenderer.setColor(color);

        //stern
        shapeRenderer.triangle(a[0],a[1],a[2],a[3],a[4],a[5]);
        shapeRenderer.triangle(a[0],a[1],a[14],a[15],a[4],a[5]);
        //middle
        shapeRenderer.triangle(a[4],a[5],a[12],a[13],a[6],a[7]);
        shapeRenderer.triangle(a[4],a[5],a[14],a[15],a[12],a[13]);
        //bow
        shapeRenderer.triangle(a[10],a[11],a[8],a[9],a[6],a[7]);
        shapeRenderer.triangle(a[10],a[11],a[12],a[13],a[6],a[7]);
    }

    protected void renderRectangleFilled(ShapeRenderer shapeRenderer, float[] a){

        shapeRenderer.triangle(a[0],a[1], a[2],a[3], a[4],a[5]);
        shapeRenderer.triangle(a[0],a[1],a[6],a[7],a[4],a[5]);
    }


}
