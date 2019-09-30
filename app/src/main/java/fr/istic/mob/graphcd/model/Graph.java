package fr.istic.mob.graphcd.model;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Graph {

    private String description;
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph (String description, List<Node> nodes) {

        this.description = description;
        this.nodes = nodes;
        this.edges = null;
    }

    public Graph (String description, List<Node> nodes, List<Edge> edges) {

        this.description = description;
        this.nodes = nodes;
        this.edges = edges;
    }

    public String getDescription() {
        return description;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void clearNodes()
    {
        this.nodes.clear();
    }

    public void clearEdges()
    {
        this.edges.clear();
    }

    public void deleteNode(Node node) {
        this.nodes.remove(node);
    }

}
