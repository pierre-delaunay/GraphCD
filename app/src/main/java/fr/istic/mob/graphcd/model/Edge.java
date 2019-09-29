package fr.istic.mob.graphcd.model;

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
    private float[] midPoint;
    private PathMeasure pathMeasure;

    /**
     * Constructors
     */

    public Edge() { }

    public Edge(Node startingNode, Node endingNode) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
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

    /**
     * Class methods
     */

    private void initPath(){
        this.path = new Path();
        path.moveTo(startingNode.getRect().centerX(), startingNode.getRect().centerY());
        path.lineTo(endingNode.getCoordX(), endingNode.getCoordY());
    }



}
