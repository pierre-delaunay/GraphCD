package fr.istic.mob.graphcd.model;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.google.gson.annotations.SerializedName;

/**
 * Loop model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Loop extends Edge {

    @SerializedName("radius")
    private float radius;

    /**
     * Constructor 1
     * @param startingNode, Node
     * @param endingNode, Node
     * @param thumbnail, String name of the edge
     * @param color, int color code
     */
    public Loop(Node startingNode, Node endingNode, String thumbnail, int color) {
        super(startingNode, endingNode, thumbnail, color);
        if (!startingNode.equals(endingNode)) throw new AssertionError("startingNode must be equal to endingNode");
        initLoop();
    }

    /**
     * Accessors and mutators
     */

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public Path getPath(){
        initLoop();
        return super.path;
    }

    /**
     * Class methods
     */

    public void initLoop() {
        super.path = new Path();
        super.arrowPath = new Path();
        super.midPoint = new PointF();

        this.radius = startingNode.getRect().width() / 2;
        float x = (startingNode.getRect().width() / 2) + startingNode.getGravityCenter().x;
        super.midPoint.x = (radius + x);
        super.midPoint.y = startingNode.getGravityCenter().y;
        super.path.addCircle(x,startingNode.getGravityCenter().y, radius , Path.Direction.CCW);

        super.rectThumbnail = new RectF(getMidPoint().x - thumbnailSize * thumbnailLength,
                getMidPoint().y -(10*(this.thumbnailSize)),
                getMidPoint().x + thumbnailSize * thumbnailLength,
                getMidPoint().y + thumbnailSize);
    }
}
