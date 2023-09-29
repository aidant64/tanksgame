package Physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;


public class Calc {

    public static float distanceBetween(Position p1, Position p2){
        float x = Math.abs(p2.getX() - p1.getX());
        float y = Math.abs(p2.getY() - p1.getY());

        return (float) Math.sqrt(x*x + y*y);
    }

    public static boolean isApproxZero(float a, float b){
        return Math.abs(a) < b;
    }

    public static boolean isApproxZero(float a){
        return Math.abs(a) < 0.001;
    }

    public static Color makeColor(int hex){
        java.awt.Color c = new java.awt.Color(hex);

        return new Color(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1);
    }

    public static int getQuadrant(float degree){
        if(degree >= 0 && degree < 90) return 1;
        else if(degree >= 90 && degree < 180) return 2;
        else if(degree >= 180 && degree < 270) return 3;
        else if(degree >= 270 && degree < 360) return 4;
        else return -1;
    }

    public static boolean overlaps(Polygon polygon, Circle circle) {
        float[] vertices=polygon.getTransformedVertices();
        Vector2 center=new Vector2(circle.x, circle.y);
        float squareRadius=circle.radius*circle.radius;
        for (int i=0;i<vertices.length;i+=2){
            if (i==0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2],
                                vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center,
                        squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]),
                        new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(circle.x, circle.y);
    }

    public static Polygon makeRectangleBounds(float x, float y, float w, float h){

        float[] vertices = {
                x - w / 2, y + h / 2,
                x - w / 2, y - h / 2,

                x + w / 2, y - h / 2,
                x + w / 2, y + h / 2
        };

        return new Polygon(vertices);

    }


}
