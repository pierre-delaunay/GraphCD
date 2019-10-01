package fr.istic.mob.graphcd.model;
import android.graphics.Color;
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

    /**
     * Constructor 1
     * @param description, String name/author
     */
    public Graph(String description) {

        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

        Node node1 = new Node(250,1150, "magentanode1", Color.MAGENTA, 50);
        Node node2 = new Node(900,1150,"graynode21", Color.GRAY, 50);
        nodes.add(node1);
        nodes.add(node2);
        edges.add(new Edge(node1, node2, "myEdge", Color.RED));

        nodes.add(new Node(550,550, "bluenode1", Color.BLUE, 50));
        nodes.add(new Node(400,400,"bluenode2", Color.BLUE, 50));
        nodes.add(new Node(490,1000,"blacknode1", Color.BLACK, 50));
        nodes.add(new Node(190,200,"blacknode2", Color.BLACK, 50));
        nodes.add(new Node(890,120, "n1", Color.BLACK, 50));
        nodes.add(new Node(50,350, "n2", Color.CYAN, 50));
        nodes.add(new Node(750,450, "голубой узел", Color.CYAN, 50));
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

    public void clearNodes()
    {
        this.nodes.clear();
    }

    public void clearEdges()
    {
        this.edges.clear();
    }

    /**
     * Class methods
     */

    public void deleteNode(Node node) {
        this.nodes.remove(node);
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }
}
