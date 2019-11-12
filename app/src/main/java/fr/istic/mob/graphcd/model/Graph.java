package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

/**
 * Graph model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Graph {

    @SerializedName("description")
    private String description;
    @SerializedName("nodes")
    private Set<Node> nodes;
    @SerializedName("edges")
    private Set<Edge> edges;

    /**
     * Constructor 1
     * @param description, author
     * @param width, width of the screen
     * @param height, height of the screen
     */
    public Graph(String description, float width, float height) {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
        this.description = description;

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

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge);
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

    private void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public String toString() {
        int loopCount = 0;
        for (Edge edge : this.edges) {
            if (edge instanceof Loop) {
                loopCount++;
            }
        }
        return "Description : " + this.description + ", Nodes : " + this.nodes.size() + ", Edges : " + this.edges.size()
                + ", Loops : " + loopCount;
    }
}
