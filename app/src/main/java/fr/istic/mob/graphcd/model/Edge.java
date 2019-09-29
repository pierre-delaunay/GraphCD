package fr.istic.mob.graphcd.model;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

public class Edge {

    public Node startingNode, endingNode;
    public Path path;
    public float[] midPoint;
    public PathMeasure pathMeasure;

    public Edge() { }

    public Edge(Node startingNode, Node endingNode) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        initPath();

    }

    public Path getPath() {
        return this.path;
    }

    private void initPath(){
        this.path = new Path();
        path.moveTo(startingNode.getRect().centerX(), startingNode.getRect().centerY());
        path.lineTo(endingNode.getCoordX(), endingNode.getCoordY());
    }


}
