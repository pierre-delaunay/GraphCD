package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Graph model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Graph {

    private String description;
    private List<Node> nodes;
    private List<Edge> edges;

    /**
     * Constructor 1
     * @param description, author
     * @param width, width of the screen
     * @param height, height of the screen
     */
    public Graph(String description, float width, float height) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

        float heightDiv, widthDiv;
        heightDiv = (height / 3) / 2;

        for (int i = 0; i < 3; i++) {
            widthDiv = (width / 3) / 2;
            for (int j = 0; j < 3; j++) {

                Node newNode = new Node(widthDiv, heightDiv, "node", Color.RED, 50);
                this.addNode(newNode);
                widthDiv += width / 3;
            }
            heightDiv += height / 4;
        }
    }

    /**
     * Accessors and mutators
     */

    public String getDescription() {
        return description;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void removeEdge(Edge edge) {
        if (this.edges.contains(edge)) {
            this.edges.remove(edge);
        }
    }

    public void clearNodes() {
        this.nodes.clear();
    }

    public void clearEdges() {
        this.edges.clear();
    }

    /**
     * Class methods
     */

    public void deleteNode(Node node) {
        this.nodes.remove(node);
    }

    public void deleteEdge(Edge edge) {
        this.edges.remove(edge);
    }
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }
}
