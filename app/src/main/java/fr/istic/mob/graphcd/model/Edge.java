package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

/**
 * Edge model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Edge {

    private Node startingNode, endingNode;
    private Path path;
    private String thumbnail;
    private int color;
    private PointF midPoint;
    private PathMeasure pathMeasure;

    /**
     * Constructors
     */

    public Edge(Node startingNode, Node endingNode, String thumbnail, int color) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.thumbnail = thumbnail;
        this.color = color;
        initPath();
    }

    /**
     * Accessors and mutators
     */

    public Path getPath() {
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

    /**
     * Class methods
     */

    private void initPath(){
        this.path = new Path();
        this.midPoint = new PointF();

        path.moveTo(startingNode.getCoordX(), startingNode.getCoordY());
        path.lineTo(endingNode.getCoordX(), endingNode.getCoordY());

        float[] middlePoint = {0f, 0f};
        float[] tangent = {0f, 0f};
        this.pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 0.50f, middlePoint, tangent);
        this.midPoint.set(middlePoint[0], middlePoint[1]);

        path.quadTo(midPoint.x, midPoint.y, endingNode.getCoordX(), endingNode.getCoordY());
    }
}
