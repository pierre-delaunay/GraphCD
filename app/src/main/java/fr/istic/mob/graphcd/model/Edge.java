package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Edge model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Edge {

    private Node startingNode, endingNode;
    private Path path;
    private String thumbnail;
    private int color, thickness;
    private PointF midPoint;
    private PathMeasure pathMeasure;
    private Region region;
    private RectF rectThumbnail;
    private int thumbnailSize;
    public static int MIN_THICKNESS_VALUE = 15;
    public static int MAX_THICKNESS_VALUE = 40;

    /**
     * Constructor 1
     * @param startingNode
     * @param endingNode
     * @param thumbnail
     * @param color
     */
    public Edge(Node startingNode, Node endingNode, String thumbnail, int color) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.thumbnail = thumbnail;
        this.thickness = MIN_THICKNESS_VALUE;
        this.color = color;
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

    public void setPath(Path path) {
        this.path = path;
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

    public Node getStartingNode() {
        return startingNode;
    }

    public void setStartingNode(Node startingNode) {
        this.startingNode = startingNode;
    }

    public Node getEndingNode() {
        return endingNode;
    }

    public void setEndingNode(Node endingNode) {
        this.endingNode = endingNode;
    }

    public PointF getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(PointF midPoint) {
        this.midPoint = midPoint;
    }

    public RectF getRectThumbnail() {
        return rectThumbnail;
    }

    public void setRectThumbnail(RectF rectThumbnail) {
        this.rectThumbnail = rectThumbnail;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
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
        path.lineTo(endingNode.getCoordX(), endingNode.getCoordY());
        float[] middlePoint = {0f, 0f};
        float[] tangent = {0f, 0f};
        this.pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 0.50f, middlePoint, tangent);
        this.midPoint.set(middlePoint[0], middlePoint[1]);

        this.rectThumbnail = new RectF(getMidPoint().x,
                getMidPoint().y,
                getMidPoint().x + 75, getMidPoint().y + 75);

        //path.quadTo(midPoint.x, midPoint.y, endingNode.getCoordX(), endingNode.getCoordY());
    }
}
