package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Edge model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Edge {

    protected Node startingNode, endingNode;
    protected Path path;
    protected String thumbnail;
    protected int color, thickness;
    protected PointF midPoint;
    protected PathMeasure pathMeasure;
    protected Region region;
    protected RectF rectThumbnail;
    protected int thumbnailSize, thumbnailLength;
    protected float x, y;
    public static int MIN_THICKNESS_VALUE = 15;
    public static int MAX_THICKNESS_VALUE = 40;
    public static int DEFAULT_THUMBNAIL_SIZE = 10;
    public static int DEFAULT_EDGE_COLOR = Color.DKGRAY;

    /**
     * Constructor 1
     * @param startingNode, Node
     * @param endingNode, Node
     * @param thumbnail, String name of the edge
     * @param color, int color code
     */
    public Edge(Node startingNode, Node endingNode, String thumbnail, int color) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.thumbnail = thumbnail;
        this.thickness = MIN_THICKNESS_VALUE;
        this.thumbnailSize = DEFAULT_THUMBNAIL_SIZE;
        this.thumbnailLength = thumbnail.length();
        this.color = color;
        this.x = 0;
        this.y = 0;
        initPath();
    }

    public Edge () { }

    /**
     * Accessors and mutators
     */

    public Path getPath() {
        initPath();
        return this.path;
    }

    public void setPath(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PointF getMidPoint() {
        return midPoint;
    }

    public RectF getRectThumbnail() {
        return rectThumbnail;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    /**
     * Class methods
     */

    private void initPath(){
        this.path = new Path();
        this.midPoint = new PointF();
        this.region = new Region();
        this.rectThumbnail = new RectF(getMidPoint().x, getMidPoint().y,
                getMidPoint().x + 15, getMidPoint().y + 15);
        path.moveTo(startingNode.getCoordX(), startingNode.getCoordY());

        if (x == 0 && y == 0) {
            this.path.quadTo(startingNode.getGravityCenter().x, startingNode.getGravityCenter().y
                    , endingNode.getGravityCenter().x, endingNode.getGravityCenter().y);
        } else {
            this.path.quadTo(x, y, endingNode.getGravityCenter().x, endingNode.getGravityCenter().y);
        }

        path.lineTo(endingNode.getCoordX(), endingNode.getCoordY());

        float[] middlePoint = {0f, 0f};
        float[] tangent = {0f, 0f};
        this.pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 0.50f, middlePoint, tangent);
        this.midPoint.set(middlePoint[0], middlePoint[1]);

        this.rectThumbnail = new RectF(getMidPoint().x - thumbnailSize * thumbnailLength,
                getMidPoint().y -(3*(this.thumbnailSize)),
                getMidPoint().x + thumbnailSize * thumbnailLength,
                getMidPoint().y + thumbnailSize);
    }
}
